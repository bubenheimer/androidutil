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
import org.bubenheimer.android.util.databinding.PreferenceDialogNumberPickerBinding

class NumberPickerPreferenceDialogFragment : PreferenceDialogFragmentCompat() {
    companion object {
        internal fun newInstance(key: String?) = NumberPickerPreferenceDialogFragment().apply {
            arguments = bundleOf(ARG_KEY to key)
        }

        private const val SAVE_STATE_NUMBER = "NumberPickerPreferenceDialogFragment.text"
    }

    private var value = 0

    private lateinit var binding: PreferenceDialogNumberPickerBinding

    private val numberPickerPreference: NumberPickerPreference
        get() = preference as NumberPickerPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        value = savedInstanceState?.getInt(SAVE_STATE_NUMBER, NumberPickerPreference.DEFAULT_VALUE)
                ?: numberPickerPreference.value
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SAVE_STATE_NUMBER, binding.numberPicker.value)
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        binding = PreferenceDialogNumberPickerBinding.bind(view)
        val numberPicker = binding.numberPicker
        val preference = numberPickerPreference
        numberPicker.minValue = preference.min
        numberPicker.maxValue = preference.max
        numberPicker.value = value
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            value = binding.numberPicker.value
            val preference = numberPickerPreference
            if (preference.callChangeListener(value)) {
                preference.value = value
            }
        }
    }
}
