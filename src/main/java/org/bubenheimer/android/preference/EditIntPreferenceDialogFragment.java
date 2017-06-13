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

import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;

public class EditIntPreferenceDialogFragment extends EditNumberPreferenceDialogFragment {
    protected void onBindEditText(final EditText editText) {
        editText.setKeyListener(new DigitsKeyListener(true, false));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
    }

    protected void checkNumberValid(final CharSequence text) throws NumberFormatException {
        final int number = Integer.parseInt(text.toString());
        final EditIntPreference preference = getEditIntPreference();
        if (preference.min > number || number > preference.max) {
            throw new NumberFormatException();
        }
    }

    private EditIntPreference getEditIntPreference() {
        return (EditIntPreference) getPreference();
    }
}
