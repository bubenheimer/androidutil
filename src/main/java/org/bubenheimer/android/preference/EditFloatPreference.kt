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
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.preference.PreferenceDialogFragmentCompat
import org.bubenheimer.android.log.Log

open class EditFloatPreference(context: Context, attrs: AttributeSet?) :
    EditNumberPreference(context, attrs) {
    private companion object {
        private val TAG = EditFloatPreference::class.simpleName!!
    }

    @UiThread
    override fun persistString(value: String?): Boolean {
        if (value == null) {
            return true
        }

        val number: Float = try {
            value.toFloat()
        } catch (e: NumberFormatException) {
            Log.e(TAG, "Invalid number: $value")
            Toast.makeText(context, "Invalid number: $value", Toast.LENGTH_LONG).show()
            return false
        }
        return persistFloat(number)
    }

    override fun getPersistedString(defaultReturnValue: String?): String? {
        if (!shouldPersist()) {
            return defaultReturnValue
        }

        val sharedPreferences = preferenceManager.sharedPreferences
        val key = key
        if (!sharedPreferences.contains(key)) {
            return defaultReturnValue
        }

        //the default will never be used - we've covered the case of value absence above
        return sharedPreferences.getFloat(key, Float.NaN).toString()
    }

    override fun newDialog(): PreferenceDialogFragmentCompat =
        EditFloatPreferenceDialogFragment.newInstance(key)
}
