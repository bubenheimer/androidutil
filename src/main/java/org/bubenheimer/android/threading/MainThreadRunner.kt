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

package org.bubenheimer.android.threading

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.os.Message

private val MAIN_HANDLER = Handler(Looper.getMainLooper())

public fun isMainThread(): Boolean = Thread.currentThread() === MAIN_HANDLER.looper.thread

public fun post(async: Boolean = true, runnable: () -> Unit) {
    if (Thread.currentThread() === MAIN_HANDLER.looper.thread) {
        runnable()
    } else {
        forcePost(async, runnable)
    }
}

@Suppress("unused")
public fun postDelayed(delayMs: Long, async: Boolean = true, runnable: () -> Unit) {
    if (delayMs <= 0L && Thread.currentThread() === MAIN_HANDLER.looper.thread) {
        runnable()
    } else {
        forcePostDelayed(delayMs, async, runnable)
    }
}

public fun forcePost(async: Boolean = true, runnable: () -> Unit) {
    obtainMessage(async, runnable).sendToTarget()
}

public fun forcePostDelayed(delayMs: Long, async: Boolean = true, runnable: () -> Unit) {
    val msg = obtainMessage(async, runnable)
    msg.target.sendMessageDelayed(msg, delayMs)
}

@Suppress("unused")
public fun postAtFrontOfQueue(async: Boolean = true, runnable: () -> Unit) {
    if (Thread.currentThread() === MAIN_HANDLER.looper.thread) {
        runnable()
    } else {
        forcePostAtFrontOfQueue(async, runnable)
    }
}

public fun forcePostAtFrontOfQueue(async: Boolean = true, runnable: () -> Unit) {
    val msg = obtainMessage(async, runnable)
    val result = msg.target.sendMessageAtFrontOfQueue(msg)
    check(result)
}

@SuppressLint("NewApi")
private fun obtainMessage(async: Boolean, runnable: () -> Unit): Message {
    val msg = Message.obtain(MAIN_HANDLER, runnable)
    if (async) msg.isAsynchronous = true
    return msg
}
