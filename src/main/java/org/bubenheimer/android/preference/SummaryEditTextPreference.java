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
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.EditTextPreferenceDialogFragmentCompat;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.util.AttributeSet;

public class SummaryEditTextPreference extends EditTextPreference implements DialogSupporter {
    @SuppressWarnings("WeakerAccess")
    public SummaryEditTextPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);
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

    @Override
    public void setText(final String text) {
        super.setText(text);
        // Update summary
        notifyChanged();
    }

    @Override
    public PreferenceDialogFragmentCompat newDialog() {
        return EditTextPreferenceDialogFragmentCompat.newInstance(getKey());
    }
}
