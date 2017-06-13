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
import android.support.annotation.RestrictTo;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.util.AttributeSet;

import org.bubenheimer.android.ui.StyleUtils;
import org.bubenheimer.android.util.R;

public final class NumberPickerPreference extends DialogPreference implements DialogSupporter {
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    static final int DEFAULT_VALUE = 0;

    private final int min;
    private final int max;

    private int value = DEFAULT_VALUE;

    public NumberPickerPreference(final Context context, final AttributeSet attrs,
                                  final int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.NumberPickerPreference, 0, 0);
        try {
            min = a.getInteger(R.styleable.NumberPickerPreference_min, Integer.MIN_VALUE);
            max = a.getInteger(R.styleable.NumberPickerPreference_max, Integer.MAX_VALUE);
        } finally {
            a.recycle();
        }
    }

    public NumberPickerPreference(final Context context, final AttributeSet attrs,
                                  final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public NumberPickerPreference(final Context context, final AttributeSet attrs) {
        this(context, attrs, StyleUtils.getAttr(
                context, R.attr.numberPickerPreferenceStyle,
                R.attr.numberPickerPreferenceStyle));
    }

    public NumberPickerPreference(final Context context) {
        this(context, null);
    }

    @SuppressWarnings("WeakerAccess")
    public int getValue() {
        return value;
    }

    @SuppressWarnings("WeakerAccess")
    public void setValue(final int value) {
        this.value = value;
        persistInt(value);
        setSummary(String.valueOf(value));
    }

    @SuppressWarnings("WeakerAccess")
    public int getMin() {
        return min;
    }

    @SuppressWarnings("WeakerAccess")
    public int getMax() {
        return max;
    }

    @Override
    protected Object onGetDefaultValue(final TypedArray a, final int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    protected void onSetInitialValue(final boolean restorePersistedValue,
                                     final Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedInt(value) : (int) defaultValue);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.value = getValue();
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
        setValue(myState.value);
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

    @Override
    public PreferenceDialogFragmentCompat newDialog() {
        return NumberPickerPreferenceDialogFragment.newInstance(getKey());
    }
}
