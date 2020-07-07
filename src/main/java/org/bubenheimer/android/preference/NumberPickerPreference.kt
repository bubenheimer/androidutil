/*
 * Copyright (c) 2015-2019 Uli Bubenheimer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.bubenheimer.android.preference

import android.content.Context
import android.content.res.TypedArray
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.annotation.RestrictTo
import androidx.core.content.withStyledAttributes
import androidx.preference.DialogPreference
import org.bubenheimer.android.util.R

class NumberPickerPreference @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.numberPickerPreferenceStyle,
        defStyleRes: Int = 0
) : DialogPreference(context, attrs, defStyleAttr, defStyleRes), DialogSupporter {
    internal companion object {
        @RestrictTo(RestrictTo.Scope.LIBRARY)
        internal const val DEFAULT_VALUE = 0
    }

    internal val min: Int
    internal val max: Int

    init {
        var tmpMin = 0
        var tmpMax = 0
        context.withStyledAttributes(attrs, R.styleable.NumberPickerPreference) {
            tmpMin = getInteger(R.styleable.NumberPickerPreference_min, Int.MIN_VALUE)
            tmpMax = getInteger(R.styleable.NumberPickerPreference_max, Int.MAX_VALUE)
        }
        min = tmpMin
        max = tmpMax
    }

    var value = DEFAULT_VALUE
        set(value) {
            field = value
            persistInt(value)
            // Update summary
            notifyChanged()
        }

    override fun getSummary(): CharSequence? =
            super.getSummary()?.let { String.format(it.toString(), value) }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any =
            a.getInteger(index, DEFAULT_VALUE)

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any) {
        value = if (restorePersistedValue) getPersistedInt(value) else defaultValue as Int
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        // No need to save instance state since it's persistent
        return if (isPersistent) superState
        else SavedState(superState).apply { value = this@NumberPickerPreference.value }
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        // Check whether we saved the state in onSaveInstanceState
        if (state.javaClass == SavedState::class.java) {
            // Cast state to custom BaseSavedState and pass to superclass
            val myState = state as SavedState
            super.onRestoreInstanceState(myState.superState)
            value = myState.value
        } else {
            // Didn't save the state, so call superclass
            super.onRestoreInstanceState(state)
        }
    }

    override fun newDialog() = NumberPickerPreferenceDialogFragment.newInstance(key)

    private class SavedState : BaseSavedState {
        internal var value = 0

        internal constructor(superState: Parcelable) : super(superState)
        private constructor(source: Parcel) : super(source) {
            value = source.readInt()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(value)
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel) = SavedState(parcel)
            override fun newArray(size: Int) = arrayOfNulls<SavedState>(size)
        }
    }
}
