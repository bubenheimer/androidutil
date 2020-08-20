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

package org.bubenheimer.android.ui

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Utility method to retrieve whatever is the parent of a Fragment, whether it's
 * another Fragment or an Activity (or something else?). This is useful, for example,
 * for DialogFragments, which may hang off an Activity or another Fragment; whatever the
 * parent is can implement an interface to receive results.
 *
 * @return the parent Fragment if it exists, otherwise the non-null Context (typically an
 * Activity).
 */
public val Fragment.parent: Any get() = parentFragment ?: requireContext()

public fun Context.appToast(text: CharSequence, duration: Int): Toast =
    Toast.makeText(applicationContext, text, duration)

public fun Context.appToast(resId: Int, duration: Int): Toast =
    Toast.makeText(applicationContext, resId, duration)
