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

import android.support.v7.app.AlertDialog;
import android.support.v7.preference.EditTextPreferenceDialogFragmentCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import org.bubenheimer.android.log.Log;

public abstract class EditNumberPreferenceDialogFragment extends EditTextPreferenceDialogFragmentCompat
        implements TextWatcher {
    private static final String TAG = EditNumberPreferenceDialogFragment.class.getSimpleName();

    @Override
    protected final void onBindDialogView(final View view) {
        super.onBindDialogView(view);

        //should never be null
        final EditText editText = (EditText) view.findViewById(android.R.id.edit);
        editText.addTextChangedListener(this);
        onBindEditText(editText);
    }

    protected abstract void onBindEditText(final EditText editText);

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
        onEditTextChanged(s);
    }

    // Misc

    private void onEditTextChanged(final CharSequence text) {
        boolean enabled;
        try {
            checkNumberValid(text);
            enabled = true;
        } catch (final NumberFormatException e) {
            Log.v(TAG, "Invalid number: ", text);
            enabled = false;
        }
        //If it were not an AlertDialog, I'd need to make other changes
        final AlertDialog dialog = (AlertDialog) getDialog();
        //The dialog may not be displayed yet - was not a user input, no need to check
        if (dialog != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(enabled);
        }
    }

    protected abstract void checkNumberValid(final CharSequence text) throws NumberFormatException;
}
