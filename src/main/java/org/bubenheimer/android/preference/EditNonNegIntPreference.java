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

public final class EditNonNegIntPreference extends EditIntPreference {
    public EditNonNegIntPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs, extractMinMax(context, attrs));
    }

    private static Pair<Integer, Integer> extractMinMax(
            final Context context, final AttributeSet attrs) {
        final int min;
        final int max;
        final TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.EditNonNegIntPreference, 0, 0);
        try {
            min = a.getInteger(R.styleable.EditNonNegIntPreference_min, 0);
            max = a.getInteger(R.styleable.EditNonNegIntPreference_max, Integer.MAX_VALUE);
        } finally {
            a.recycle();
        }
        return new Pair<>(min, max);
    }

    @Override
    public PreferenceDialogFragmentCompat newDialog() {
        return EditNonNegIntPreferenceDialogFragment.newInstance(getKey());
    }
}
