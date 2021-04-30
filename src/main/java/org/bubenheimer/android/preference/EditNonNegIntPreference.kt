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
import androidx.core.content.withStyledAttributes
import androidx.preference.PreferenceDialogFragmentCompat
import org.bubenheimer.android.util.R

public class EditNonNegIntPreference(context: Context, attrs: AttributeSet) :
    EditIntPreference(context, attrs, extractMinMax(context, attrs)) {
    private companion object {
        fun extractMinMax(context: Context, attrs: AttributeSet): Pair<Int, Int> {
            var min = 0
            var max = 0

            context.withStyledAttributes(attrs, R.styleable.EditNonNegIntPreference) {
                min = getInteger(R.styleable.EditNonNegIntPreference_min, 0)
                max = getInteger(R.styleable.EditNonNegIntPreference_max, Int.MAX_VALUE)
            }

            return Pair(min, max)
        }
    }

    public override fun newDialog(): PreferenceDialogFragmentCompat =
        EditNonNegIntPreferenceDialogFragment.newInstance(key)
}
