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

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.preference.PreferenceDialogFragmentCompat
import org.bubenheimer.android.util.databinding.PreferenceDialogDurationPickerBinding

class DurationPickerPreferenceDialogFragment : PreferenceDialogFragmentCompat() {
    companion object {
        internal fun newInstance(key: String) = DurationPickerPreferenceDialogFragment().apply {
            arguments = bundleOf(ARG_KEY to key)
        }

        private const val SAVE_STATE_DURATION = "DurationPickerPreferenceDialogFragment.text"
    }

    private lateinit var binding: PreferenceDialogDurationPickerBinding

    private var value = 0

    private val dialogValue: Int
        get() {
            val days = binding.pickerDays.value
            val hours = binding.pickerHours.value
            val minutes = binding.pickerMinutes.value
            return (days * 24 + hours) * 60 + minutes
        }

    private val durationPickerPreference: DurationPickerPreference
        get() = preference as DurationPickerPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        value = savedInstanceState
                ?.getInt(SAVE_STATE_DURATION, DurationPickerPreference.DEFAULT_VALUE)
                ?: durationPickerPreference.value
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        binding = PreferenceDialogDurationPickerBinding.bind(view)

        val daysPicker = binding.pickerDays
        daysPicker.minValue = 0
        daysPicker.maxValue = 30
        daysPicker.value = DurationPickerPreference.getDays(value)

        val hoursPicker = binding.pickerHours
        hoursPicker.minValue = 0
        hoursPicker.maxValue = 23
        hoursPicker.value = DurationPickerPreference.getHours(value)

        val minutesPicker = binding.pickerMinutes
        minutesPicker.minValue = 0
        minutesPicker.maxValue = 59
        minutesPicker.value = DurationPickerPreference.getMinutes(value)
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            value = dialogValue

            val preference = durationPickerPreference
            if (preference.callChangeListener(value)) {
                preference.value = value
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SAVE_STATE_DURATION, dialogValue)
    }
}
