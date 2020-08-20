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

import android.content.Context
import android.util.AttributeSet
import androidx.preference.EditTextPreference
import androidx.preference.EditTextPreferenceDialogFragmentCompat
import androidx.preference.PreferenceDialogFragmentCompat

public open class SummaryEditTextPreference(context: Context, attrs: AttributeSet?) :
        EditTextPreference(context, attrs), DialogSupporter {
    public override fun getSummary(): CharSequence? =
            super.getSummary()?.let { String.format(it.toString(), text ?: "") }

    public override fun setText(text: String) {
        super.setText(text)
        // Update summary
        notifyChanged()
    }

    public override fun newDialog(): PreferenceDialogFragmentCompat =
            EditTextPreferenceDialogFragmentCompat.newInstance(key)
}
