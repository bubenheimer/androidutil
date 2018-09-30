/*
 * Copyright (c) 2015-2017 Uli Bubenheimer.
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

import org.bubenheimer.android.util.databinding.PreferenceDialogDurationPickerBinding;

@SuppressWarnings("unused")
public final class DurationPickerPreferenceDialogFragment extends PreferenceDialogFragmentCompat {
    private static final String SAVE_STATE_DURATION = "DurationPickerPreferenceDialogFragment.text";

    private PreferenceDialogDurationPickerBinding binding;

    private int value;

    public static DurationPickerPreferenceDialogFragment newInstance(final String key) {
        final DurationPickerPreferenceDialogFragment fragment =
                new DurationPickerPreferenceDialogFragment();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            value = getDurationPickerPreference().getValue();
        } else {
            value = savedInstanceState.getInt(SAVE_STATE_DURATION,
                    DurationPickerPreference.DEFAULT_VALUE);
        }
    }

    @Override
    protected void onBindDialogView(final View view) {
        super.onBindDialogView(view);

        binding = PreferenceDialogDurationPickerBinding.bind(view);

        setDialogValue(value);
    }

    private void setDialogValue(final int value) {
        final NumberPicker daysPicker = binding.pickerDays;
        daysPicker.setMinValue(0);
        daysPicker.setMaxValue(30);
        daysPicker.setValue(DurationPickerPreference.getDays(value));

        final NumberPicker hoursPicker = binding.pickerHours;
        hoursPicker.setMinValue(0);
        hoursPicker.setMaxValue(23);
        hoursPicker.setValue(DurationPickerPreference.getHours(value));

        final NumberPicker minutesPicker = binding.pickerMinutes;
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        minutesPicker.setValue(DurationPickerPreference.getMinutes(value));
    }

    @Override
    public void onDialogClosed(final boolean positiveResult) {
        if (positiveResult) {
            value = getDialogValue();
            final DurationPickerPreference preference = getDurationPickerPreference();
            if (preference.callChangeListener(value)) {
                preference.setValue(value);
            }
        }
    }

    private int getDialogValue() {
        final int days = binding.pickerDays.getValue();
        final int hours = binding.pickerHours.getValue();
        final int minutes = binding.pickerMinutes.getValue();

        return (days * 24 + hours) * 60 + minutes;
    }

    @Override
    public void onSaveInstanceState(final @NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_STATE_DURATION, getDialogValue());
    }

    private DurationPickerPreference getDurationPickerPreference() {
        return (DurationPickerPreference) getPreference();
    }
}
