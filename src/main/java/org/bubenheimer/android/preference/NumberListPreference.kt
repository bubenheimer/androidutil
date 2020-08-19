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
import androidx.preference.ListPreference
import org.bubenheimer.android.log.Log.e

@Suppress("unused")
class NumberListPreference : ListPreference {
    private companion object {
        private val TAG = NumberListPreference::class.simpleName!!
    }

    @Suppress("unused")
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes)
    @Suppress("unused")
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)
    @Suppress("unused")
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    @Suppress("unused")
    constructor(context: Context?) : super(context)

    override fun persistString(value: String?): Boolean {
        if (value == null) {
            return true
        }

        val number = try {
            value.toInt()
        } catch (e: NumberFormatException) {
            e(TAG, "Invalid number: $value")
            Toast.makeText(context, "Invalid number: $value", Toast.LENGTH_LONG).show()
            return false
        }

        return persistInt(number)
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
        return sharedPreferences.getInt(key, 0).toString()
    }
}