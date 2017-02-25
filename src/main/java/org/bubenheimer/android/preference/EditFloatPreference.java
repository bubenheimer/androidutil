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
import android.preference.EditTextPreference;
import android.support.annotation.UiThread;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Toast;

import org.bubenheimer.android.log.Log;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class EditFloatPreference extends EditTextPreference {
    private static final String TAG = EditFloatPreference.class.getSimpleName();

    public EditFloatPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        final EditText editText = getEditText();
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    @UiThread
    @Override
    protected boolean persistString(final String value) {
        if (value == null) {
            return true;
        }
        final float number;
        try {
            number = Float.parseFloat(value);
        } catch (final NumberFormatException e) {
            Log.e(TAG, "Invalid number: ", value);
            Toast.makeText(getContext(), "Invalid number: " + value, Toast.LENGTH_LONG).show();
            return false;
        }
        return persistFloat(number);
    }

    @Override
    public void setText(final String text) {
        super.setText(text);
        //reliably triggers a summary refresh, otherwise it's unreliable
        notifyChanged();
    }

    @Override
    protected String getPersistedString(final String defaultReturnValue) {
        if (!shouldPersist()) {
            return defaultReturnValue;
        }

        final SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        final String key = getKey();
        if (!sharedPreferences.contains(key)) {
            return defaultReturnValue;
        }

        //the 0 default will never be used - we've covered the case of value absence above
        final float value = sharedPreferences.getFloat(key, Float.NaN);
        return Float.toString(value);
    }

    @Override
    public CharSequence getSummary() {
        final CharSequence summary = super.getSummary();
        if (summary == null) {
            return null;
        } else {
            final CharSequence value = getText();
            return String.format(summary.toString(), value == null ? "" : value);
        }
    }
}
