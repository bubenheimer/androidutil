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
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.v4.util.Pair;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.util.AttributeSet;
import android.widget.Toast;

import org.bubenheimer.android.log.Log;
import org.bubenheimer.android.util.R;

public class EditIntPreference extends EditNumberPreference implements DialogSupporter {
    private static final String TAG = EditIntPreference.class.getSimpleName();

    final int min;
    final int max;

    protected EditIntPreference(
            final Context context, final AttributeSet attrs, final Pair<Integer, Integer> defaults) {
        super(context, attrs);

        final TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.EditIntPreference, 0, 0);
        try {
            this.min = a.getInteger(R.styleable.EditIntPreference_min, defaults.first);
            this.max = a.getInteger(R.styleable.EditIntPreference_max, defaults.second);
        } finally {
            a.recycle();
        }
    }

    public EditIntPreference(final Context context, final AttributeSet attrs) {
        this(context, attrs, new Pair<>(Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    @Override
    protected final boolean persistString(final String value) {
        if (value == null) {
            return true;
        }
        final int number;
        try {
            number = Integer.parseInt(value);
        } catch (final NumberFormatException e) {
            Log.e(TAG, "Invalid number: ", value);
            Toast.makeText(getContext(), "Invalid number: " + value, Toast.LENGTH_LONG).show();
            return false;
        }
        return persistInt(number);
    }

    @Override
    protected final String getPersistedString(final String defaultReturnValue) {
        if (!shouldPersist()) {
            return defaultReturnValue;
        }

        final SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        final String key = getKey();
        if (!sharedPreferences.contains(key)) {
            return defaultReturnValue;
        }

        //the 0 default will never be used - we've covered the case of value absence above
        final int value = sharedPreferences.getInt(key, 0);
        return Integer.toString(value);
    }

    @Override
    public PreferenceDialogFragmentCompat newDialog() {
        return EditIntPreferenceDialogFragment.newInstance(getKey());
    }
}
