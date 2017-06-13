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
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.util.AttributeSet;

import org.bubenheimer.android.ui.StyleUtils;
import org.bubenheimer.android.util.R;

import java.util.Locale;

@SuppressWarnings("unused")
public final class DurationPickerPreference extends DialogPreference implements DialogSupporter {
    //Default duration: 2 hours
    static final int DEFAULT_VALUE = 120;

    private int value = DEFAULT_VALUE;

    public DurationPickerPreference(final Context context, final AttributeSet attrs,
                                  final int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public DurationPickerPreference(final Context context, final AttributeSet attrs,
                                  final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DurationPickerPreference(final Context context, final AttributeSet attrs) {
        this(context, attrs, StyleUtils.getAttr(
                context, R.attr.durationPickerPreferenceStyle,
                R.attr.durationPickerPreferenceStyle));
    }

    public DurationPickerPreference(final Context context) {
        this(context, null);
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        this.value = value;
        persistInt(value);
        notifyChanged();
    }

    static int getDays(final int minutes) {
        return minutes / (60 * 24);
    }

    static int getHours(final int minutes) {
        return minutes % (60 * 24) / 60;
    }

    static int getMinutes(final int minutes) {
        return minutes % 60;
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
        if (isPersistent()) {
            // No need to save instance state since it's persistent, use superclass state
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.value = value;
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
        setValue(myState.value);
    }

    @Override
    public PreferenceDialogFragmentCompat newDialog() {
        return DurationPickerPreferenceDialogFragment.newInstance(getKey());
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
