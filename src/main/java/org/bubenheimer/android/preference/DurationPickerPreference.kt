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
import androidx.preference.DialogPreference
import androidx.preference.PreferenceDialogFragmentCompat
import org.bubenheimer.android.util.R

public class DurationPickerPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.durationPickerPreferenceStyle,
    defStyleRes: Int = 0
) : DialogPreference(context, attrs, defStyleAttr, defStyleRes), DialogSupporter {
    internal companion object {
        //Default duration: 2 hours
        internal const val DEFAULT_VALUE = 120

        fun getDays(minutes: Int): Int {
            return minutes / (60 * 24)
        }

        fun getHours(minutes: Int): Int {
            return minutes % (60 * 24) / 60
        }

        fun getMinutes(minutes: Int): Int {
            return minutes % 60
        }
    }

    public var value: Int = DEFAULT_VALUE
        set(value) {
            field = value
            persistInt(value)
            notifyChanged()
        }

    public override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        return a.getInteger(index, DEFAULT_VALUE)
    }

    public override fun onSetInitialValue(defaultValue: Any?) {
        value = getPersistedInt(defaultValue as Int? ?: value)
    }

    public override fun getSummary(): CharSequence =
        super.getSummary() ?: (if (value == 0) "Unlimited"
        else String.format("%dd %dh %dm", getDays(value), getHours(value), getMinutes(value)))

    public override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()

        // Check whether this Preference is persistent (continually saved)

        // No need to save instance state since it's persistent, use superclass state
        return if (isPersistent) superState
        else SavedState(superState).apply { value = this@DurationPickerPreference.value }
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        // Check whether we saved the state in onSaveInstanceState
        if (state.javaClass != SavedState::class.java) {
            // Didn't save the state, so call superclass
            super.onRestoreInstanceState(state)
            return
        }

        // Cast state to custom BaseSavedState and pass to superclass
        val myState = state as SavedState

        super.onRestoreInstanceState(myState.superState)

        // Set this Preference's widget to reflect the restored state; the widget better exist
        value = myState.value
    }

    public override fun newDialog(): PreferenceDialogFragmentCompat =
        DurationPickerPreferenceDialogFragment.newInstance(key)

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
