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

package org.bubenheimer.android.preference;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceDialogFragmentCompat;
import android.view.View;
import android.widget.NumberPicker;

import org.bubenheimer.android.util.databinding.PreferenceDialogNumberPickerBinding;

public final class NumberPickerPreferenceDialogFragment
        extends PreferenceDialogFragmentCompat {
    private static final String SAVE_STATE_NUMBER = "NumberPickerPreferenceDialogFragment.text";

    private int value;
    private PreferenceDialogNumberPickerBinding binding;

    public static NumberPickerPreferenceDialogFragment newInstance(final String key) {
        final NumberPickerPreferenceDialogFragment fragment =
                new NumberPickerPreferenceDialogFragment();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            value = getNumberPickerPreference().getValue();
        } else {
            value = savedInstanceState.getInt(
                    SAVE_STATE_NUMBER, NumberPickerPreference.DEFAULT_VALUE);
        }
    }

    @Override
    public void onSaveInstanceState(final @NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_STATE_NUMBER, binding.numberPicker.getValue());
    }

    @Override
    protected void onBindDialogView(final View view) {
        super.onBindDialogView(view);

        binding = PreferenceDialogNumberPickerBinding.bind(view);

        final NumberPicker numberPicker = binding.numberPicker;
        final NumberPickerPreference preference = getNumberPickerPreference();
        numberPicker.setMinValue(preference.getMin());
        numberPicker.setMaxValue(preference.getMax());
        numberPicker.setValue(value);
    }

    @Override
    public void onDialogClosed(final boolean positiveResult) {
        if (positiveResult) {
            value = binding.numberPicker.getValue();
            final NumberPickerPreference preference = getNumberPickerPreference();
            if (preference.callChangeListener(value)) {
                preference.setValue(value);
            }
        }
    }

    private NumberPickerPreference getNumberPickerPreference() {
        return (NumberPickerPreference) getPreference();
    }
}
