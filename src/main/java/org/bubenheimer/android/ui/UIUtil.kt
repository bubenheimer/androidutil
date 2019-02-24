/*
 * Copyright (c) 2015-2019 Uli Bubenheimer. All rights reserved.
 */

@file:JvmName("UIUtil")

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
fun getFragmentParent(fragment: Fragment) =
    fragment.parentFragment ?: fragment.requireContext()

fun appToast(context: Context, text: CharSequence, duration: Int) =
        Toast.makeText(context.applicationContext, text, duration)

fun appToast(context: Context, resId: Int, duration: Int) =
        Toast.makeText(context.applicationContext, resId, duration)
