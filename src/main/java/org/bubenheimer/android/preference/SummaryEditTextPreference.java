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
import android.content.res.TypedArray;
import android.support.v4.util.Pair;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.util.AttributeSet;

import org.bubenheimer.android.util.R;

@SuppressWarnings("unused")
public final class SummaryEditTextPreference extends EditTextPreference implements DialogSupporter {
    final int minLength;
    final int maxLength;

    protected SummaryEditTextPreference(
            final Context context, final AttributeSet attrs, final Pair<Integer, Integer> defaults) {
        super(context, attrs);

        final TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.SummaryEditTextPreference, 0, 0);
        try {
            this.minLength =
                    a.getInteger(R.styleable.SummaryEditTextPreference_minLen, defaults.first);
            this.maxLength =
                    a.getInteger(R.styleable.SummaryEditTextPreference_maxLen, defaults.second);
        } finally {
            a.recycle();
        }
    }

    public SummaryEditTextPreference(final Context context, final AttributeSet attrs) {
        this(context, attrs, new Pair<>(0, Integer.MAX_VALUE));
    }

    @Override
    public void setText(final String text) {
        super.setText(text);
        setSummary(text);
    }

    @Override
    public PreferenceDialogFragmentCompat newDialog() {
        return SummaryEditTextPreferenceDialogFragment.newInstance(getKey());
    }
}
