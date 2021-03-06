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
package org.bubenheimer.android.preference

import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.preference.EditTextPreferenceDialogFragmentCompat
import org.bubenheimer.android.log.Log.v

public abstract class ValidatingEditTextPreferenceDialogFragment :
    EditTextPreferenceDialogFragmentCompat() {
    public companion object {
        private val TAG = ValidatingEditTextPreferenceDialogFragment::class.java.simpleName
    }

    public override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        val editText = view.findViewById<EditText>(android.R.id.edit)

        editText.doAfterTextChanged {
            checkTextValid(it!!).let { isTextValid ->
                if (!isTextValid) v(TAG, "Invalid number: $it")

                // If it were not an AlertDialog, I'd need to make other changes.
                // The dialog may not be displayed yet - was not a user input, no need to check.
                (dialog as AlertDialog?)?.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled =
                    isTextValid
            }
        }

        editText.onBindEditText()
    }

    protected open fun EditText.onBindEditText() {}

    protected open fun checkTextValid(text: CharSequence): Boolean = true
}
