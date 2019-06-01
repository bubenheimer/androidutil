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

package org.bubenheimer.android.preference;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.core.util.Pair;
import androidx.preference.PreferenceDialogFragmentCompat;
import android.util.AttributeSet;

import org.bubenheimer.android.util.R;

public final class ConstrainedEditTextPreference extends SummaryEditTextPreference {
    final int minLength;
    final int maxLength;

    @SuppressWarnings("WeakerAccess")
    protected ConstrainedEditTextPreference(
            final Context context, final AttributeSet attrs, final Pair<Integer, Integer> defaults) {
        super(context, attrs);

        final TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.ConstrainedEditTextPreference, 0, 0);
        try {
            this.minLength =
                    a.getInteger(R.styleable.ConstrainedEditTextPreference_minLen, defaults.first);
            this.maxLength =
                    a.getInteger(R.styleable.ConstrainedEditTextPreference_maxLen, defaults.second);
        } finally {
            a.recycle();
        }
    }

    @SuppressWarnings("unused")
    public ConstrainedEditTextPreference(final Context context, final AttributeSet attrs) {
        this(context, attrs, new Pair<>(0, Integer.MAX_VALUE));
    }

    @Override
    public PreferenceDialogFragmentCompat newDialog() {
        return ConstrainedEditTextPreferenceDialogFragment.newInstance(getKey());
    }
}
