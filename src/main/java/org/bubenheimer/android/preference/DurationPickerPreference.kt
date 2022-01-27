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

    override fun onSaveInstanceState(): Parcelable? = super.onSaveInstanceState().let {
        // No need to save instance state when preference is persistent (continuously saved),
        // use super class state
        if (isPersistent) it
        else SavedState(it ?: BaseSavedState.EMPTY_STATE).also { value = this.value }
    }

    override fun onRestoreInstanceState(state: Parcelable?): Unit =
        // Only restore state ourselves when we actually saved some
        state?.let { it as? SavedState }?.let {
            super.onRestoreInstanceState(it.superState)
            value = it.value
        } ?: super.onRestoreInstanceState(state)

    public override fun newDialog(): PreferenceDialogFragmentCompat =
        DurationPickerPreferenceDialogFragment.newInstance(key)

    private class SavedState : BaseSavedState {
        var value = 0

        constructor(superState: Parcelable) : super(superState)
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
