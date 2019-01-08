/*
 * Copyright (c) 2015-2019 Uli Bubenheimer. All rights reserved.
 */

@file:JvmName("Utility")

package org.bubenheimer.android.ui

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
