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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;

public class EditFloatPreferenceDialogFragment extends ValidatingEditTextPreferenceDialogFragment {
    public static EditFloatPreferenceDialogFragment newInstance(final String key) {
        final EditFloatPreferenceDialogFragment fragment =
                new EditFloatPreferenceDialogFragment();

        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected void onBindEditText(final @NonNull EditText editText) {
        editText.setKeyListener(new DigitsKeyListener(true, true));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
    }

    @Override
    protected void checkTextValid(@NonNull final CharSequence text) throws NumberFormatException {
        //noinspection ResultOfMethodCallIgnored
        Float.parseFloat(text.toString());
    }
}