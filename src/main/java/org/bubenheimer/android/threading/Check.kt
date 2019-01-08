/*
 * Copyright (c) 2015-2019 Uli Bubenheimer. All rights reserved.
 */

package org.bubenheimer.android.threading

import android.os.Handler
import android.os.Looper

fun Thread.isCurrent() = this === Thread.currentThread()

fun Looper.onCurrentThread() = thread.isCurrent()

fun Handler.onCurrentThread() = looper.onCurrentThread()

fun onMainThread() = Looper.getMainLooper().onCurrentThread()
