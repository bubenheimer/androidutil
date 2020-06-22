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
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import org.bubenheimer.android.log.Log

private const val TAG = "MainThreadRunner"

private val MAIN_HANDLER = Handler(Looper.getMainLooper())

internal val HAS_ASYNC: Boolean = run {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
        // Confirm the method is there
        val message = Message.obtain()
        try {
            @SuppressLint("NewApi")
            message.isAsynchronous = true
            true
        } catch (e: NoSuchMethodError) {
            Log.wx(e, TAG)
            false
        } finally {
            message.recycle()
        }
    } else {
        true
    }
}

fun isMainThread() = Thread.currentThread() === MAIN_HANDLER.looper.thread

fun post(async: Boolean = true, runnable: () -> Unit) {
    if (Thread.currentThread() === MAIN_HANDLER.looper.thread) {
        runnable()
    } else {
        forcePost(async, runnable)
    }
}

@Suppress("unused")
fun postDelayed(delayMs: Long, async: Boolean = true, runnable: () -> Unit) {
    if (delayMs <= 0L && Thread.currentThread() === MAIN_HANDLER.looper.thread) {
        runnable()
    } else {
        forcePostDelayed(delayMs, async, runnable);
    }
}

fun forcePost(async: Boolean = true, runnable: () -> Unit) {
    obtainMessage(async, runnable).sendToTarget()
}

fun forcePostDelayed(delayMs: Long, async: Boolean = true, runnable: () -> Unit) {
    val msg = obtainMessage(async, runnable)
    msg.target.sendMessageDelayed(msg, delayMs)
}

@Suppress("unused")
fun postAtFrontOfQueue(async: Boolean = true, runnable: () -> Unit) {
    if (Thread.currentThread() === MAIN_HANDLER.looper.thread) {
        runnable()
    } else {
        forcePostAtFrontOfQueue(async, runnable)
    }
}

fun forcePostAtFrontOfQueue(async: Boolean = true, runnable: () -> Unit) {
    val msg = obtainMessage(async, runnable)
    val result = msg.target.sendMessageAtFrontOfQueue(msg)
    check(result)
}

@SuppressLint("NewApi")
private fun obtainMessage(async: Boolean, runnable: () -> Unit) : Message {
    val msg = Message.obtain(MAIN_HANDLER, runnable)
    if (async && HAS_ASYNC) {
        msg.isAsynchronous = true
    }
    return msg
}
