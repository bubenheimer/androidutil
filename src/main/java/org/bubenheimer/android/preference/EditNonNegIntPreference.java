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

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Toast;

import org.bubenheimer.android.log.Log;
import org.bubenheimer.android.util.R;

public final class EditNonNegIntPreference extends EditTextPreference implements TextWatcher {
    private static final String TAG = EditNonNegIntPreference.class.getSimpleName();

    private final int min;
    private final int max;

    public EditNonNegIntPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.EditNonNegIntPreference, 0, 0);
        try {
            min = a.getInteger(R.styleable.EditNonNegIntPreference_min, 0);
            max = a.getInteger(R.styleable.EditNonNegIntPreference_max, Integer.MAX_VALUE);
        } finally {
            a.recycle();
        }

        final EditText editText = getEditText();
        editText.setKeyListener(new DigitsKeyListener(false, false));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    protected void showDialog(final Bundle state) {
        super.showDialog(state);

        getEditText().addTextChangedListener(this);
        onEditTextChanged();
    }

    @Override
    protected final void onDialogClosed(final boolean positiveResult) {
        getEditText().removeTextChangedListener(this);

        super.onDialogClosed(positiveResult);
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
    public final void setText(final String text) {
        super.setText(text);
        //reliably triggers a summary refresh, otherwise it's unreliable
        notifyChanged();
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
    public final CharSequence getSummary() {
        final CharSequence summary = super.getSummary();
        if (summary == null) {
            return null;
        } else {
            final CharSequence value = getText();
            return String.format(summary.toString(), value == null ? "" : value);
        }
    }

    // TextWatcher implementation

    @Override
    public final void onTextChanged(
            final CharSequence s, final int start, final int before, final int count) {
    }

    @Override
    public final void beforeTextChanged(
            final CharSequence s, final int start, final int before, final int count) {
    }

    @Override
    public final void afterTextChanged(final Editable s) {
        onEditTextChanged();
    }

    // Misc

    private void onEditTextChanged() {
        //If it were not an AlertDialog, I'd need to make other changes
        final AlertDialog dialog = (AlertDialog) getDialog();
        final String text = getEditText().getText().toString();
        boolean enabled;
        final int number;
        try {
            number = Integer.parseInt(text);
            enabled = min <= number && number <= max;
        } catch (final NumberFormatException e) {
            Log.v(TAG, "Invalid number: ", text);
            enabled = false;
        }
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(enabled);
    }
}
