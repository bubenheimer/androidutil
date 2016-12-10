/*
 * Copyright (c) 2015-2016 Uli Bubenheimer.
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
import org.bubenheimer.android.util.databinding.DialogNumberPickerBinding;

public final class NumberPickerPreference extends DialogPreference {
    private static final int DEFAULT_VALUE = 0;

    private DialogNumberPickerBinding binding;

    private final int min;
    private final int max;

    private int value;

    public NumberPickerPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.NumberPickerPreference, 0, 0);
        try {
            min = a.getInteger(R.styleable.NumberPickerPreference_min, Integer.MIN_VALUE);
            max = a.getInteger(R.styleable.NumberPickerPreference_max, Integer.MAX_VALUE);
        } finally {
            a.recycle();
        }

        setDialogLayoutResource(R.layout.dialog_number_picker);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(final View view) {
        super.onBindDialogView(view);

        binding = DialogNumberPickerBinding.bind(view);
        final NumberPicker numberPicker = binding.numberPicker;
        numberPicker.setMinValue(min);
        numberPicker.setMaxValue(max);
        numberPicker.setValue(value);
    }

    @Override
    protected void onDialogClosed(final boolean positiveResult) {
        if (positiveResult) {
            value = binding.numberPicker.getValue();
            persistInt(value);
            notifyChanged();
        }
        binding = null;
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
            return null;
        } else {
            return String.format(summary.toString(), value);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        // Check whether this Preference is persistent (continually saved)
        if (isPersistent() || binding == null) {
            // No need to save instance state since it's persistent or dialog not shown,
            // use superclass state
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.value = binding.numberPicker.getValue();
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
        binding.numberPicker.setValue(myState.value);
    }

    private static final class SavedState extends BaseSavedState {
        int value;

        public SavedState(final Parcelable superState) {
            super(superState);
        }

        public SavedState(final Parcel source) {
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
