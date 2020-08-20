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

import android.os.Build
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.preference.PreferenceDialogFragmentCompat

public class EditNonNegIntPreferenceDialogFragment : EditIntPreferenceDialogFragment() {
    internal companion object {
        internal fun newInstance(key: String): EditNonNegIntPreferenceDialogFragment {
            return EditNonNegIntPreferenceDialogFragment().apply {
                arguments = bundleOf(PreferenceDialogFragmentCompat.ARG_KEY to key)
            }
        }
    }

    public override fun EditText.onBindEditText() {
        keyListener = if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            DigitsKeyListener(null, false, false)
        } else {
            @Suppress("DEPRECATION")
            DigitsKeyListener(false, false)
        }
        inputType = InputType.TYPE_CLASS_NUMBER
    }
}
