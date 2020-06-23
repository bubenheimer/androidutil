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

class ConstrainedEditTextPreference private constructor(
        context: Context,
        attrs: AttributeSet?,
        defaults: Pair<Int, Int> = 0 to Int.MAX_VALUE
) : SummaryEditTextPreference(context, attrs) {
    internal val minLength: Int
    internal val maxLength: Int

    init {
        var tmpMin = 0
        var tmpMax = 0
        context.withStyledAttributes(attrs, R.styleable.ConstrainedEditTextPreference) {
            val (minLenDef, maxLenDef) = defaults
            tmpMin = getInteger(R.styleable.ConstrainedEditTextPreference_minLen, minLenDef)
            tmpMax = getInteger(R.styleable.ConstrainedEditTextPreference_maxLen, maxLenDef)
        }
        minLength = tmpMin
        maxLength = tmpMax
    }

    override fun newDialog(): PreferenceDialogFragmentCompat {
        return ConstrainedEditTextPreferenceDialogFragment.newInstance(key)
    }
}
