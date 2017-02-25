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

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

import org.bubenheimer.android.util.R;
import org.bubenheimer.android.util.databinding.DialogDurationPickerBinding;

import java.util.Locale;

@SuppressWarnings("unused")
public final class DurationPickerPreference extends DialogPreference {
    //Default duration: 2 hours
    private static final int DEFAULT_VALUE = 120;

    private DialogDurationPickerBinding binding;

    private int value;

    public DurationPickerPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.dialog_duration_picker);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(final View view) {
        super.onBindDialogView(view);

        binding = DialogDurationPickerBinding.bind(view);

        setDialogValue(value);
    }

    private void setDialogValue(final int value) {
        final NumberPicker daysPicker = binding.pickerDays;
        daysPicker.setMinValue(0);
        daysPicker.setMaxValue(30);
        daysPicker.setValue(getDays(value));

        final NumberPicker hoursPicker = binding.pickerHours;
        hoursPicker.setMinValue(0);
        hoursPicker.setMaxValue(23);
        hoursPicker.setValue(getHours(value));

        final NumberPicker minutesPicker = binding.pickerMinutes;
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        minutesPicker.setValue(getMinutes(value));
    }

    private static int getDays(final int minutes) {
        return minutes / (60 * 24);
    }

    private static int getHours(final int minutes) {
        return minutes % (60 * 24) / 60;
    }

    private static int getMinutes(final int minutes) {
        return minutes % 60;
    }

    @Override
    protected void onDialogClosed(final boolean positiveResult) {
        if (positiveResult) {
            value = getDialogValue();
            persistInt(value);
            notifyChanged();
        }

        binding = null;
    }

    private int getDialogValue() {
        final int days = binding.pickerDays.getValue();
        final int hours = binding.pickerHours.getValue();
        final int minutes = binding.pickerMinutes.getValue();

        return (days * 24 + hours) * 60 + minutes;
    }

    @Override
    protected void onSetInitialValue(final boolean restorePersistedValue,
                                     final Object defaultValue) {
        if (restorePersistedValue) {
            value = getPersistedInt(DEFAULT_VALUE);
        } else {
            // Set default state from the XML attribute
            value = (int) defaultValue;
            persistInt(value);
        }
    }

    @Override
    protected Object onGetDefaultValue(final TypedArray a, final int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    public CharSequence getSummary() {
        final CharSequence summary = super.getSummary();
        if (summary == null) {
            if (value == 0) {
                return "Unlimited";
            } else {
                return String.format((Locale) null, "%dd %dh %dm",
                        getDays(value), getHours(value), getMinutes(value));
            }
        } else {
            return summary;
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        // Check whether this Preference is persistent (continually saved)
        if (isPersistent() && binding == null) {
            // No need to save instance state since it's persistent and dialog not shown,
            // use superclass state
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.value = getDialogValue();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        // Check whether we saved the state in onSaveInstanceState
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save the state, so call superclass
            super.onRestoreInstanceState(state);
            return;
        }

        // Cast state to custom BaseSavedState and pass to superclass
        final SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        // Set this Preference's widget to reflect the restored state; the widget better exist
        setDialogValue(myState.value);
    }

    private static final class SavedState extends BaseSavedState {
        int value;

        SavedState(final Parcelable superState) {
            super(superState);
        }

        SavedState(final Parcel source) {
            super(source);
            value = source.readInt();
        }

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(value);
        }

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(final Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(final int size) {
                        return new SavedState[size];
                    }
                };
    }
}
