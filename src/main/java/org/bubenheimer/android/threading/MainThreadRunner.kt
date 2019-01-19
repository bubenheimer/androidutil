/*
 * Copyright (c) 2015-2019 Uli Bubenheimer. All rights reserved.
 */

@file:JvmName("MainThreadRunner")

package org.bubenheimer.android.threading

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import org.bubenheimer.android.log.Log

// Is there a simple better way besides hard-coding it? (and besides companion object or class)
private val TAG = "MainThreadRunner"

private val MAIN_HANDLER = Handler(Looper.getMainLooper())

private val HAS_ASYNC: Boolean by lazy {
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

@JvmOverloads
fun post(async: Boolean = true, runnable: Runnable) {
    if (Thread.currentThread() === MAIN_HANDLER.looper.thread) {
        runnable.run()
    } else {
        forcePost(async, runnable)
    }
}

@Suppress("unused")
@JvmOverloads
fun postDelayed(async: Boolean = true, runnable: Runnable, delayMs: Long) {
    if (delayMs <= 0L && Thread.currentThread() === MAIN_HANDLER.looper.thread) {
        runnable.run()
    } else {
        forcePostDelayed(async, runnable, delayMs);
    }
}

@JvmOverloads
fun forcePost(async: Boolean = true, runnable: Runnable) {
    obtainMessage(async, runnable).sendToTarget()
}

@JvmOverloads
fun forcePostDelayed(async: Boolean = true, runnable: Runnable, delayMs: Long) {
    val msg = obtainMessage(async, runnable)
    msg.target.sendMessageDelayed(msg, delayMs)
}

@Suppress("unused")
@JvmOverloads
fun postAtFrontOfQueue(async: Boolean = true, runnable: Runnable) {
    if (Thread.currentThread() === MAIN_HANDLER.looper.thread) {
        runnable.run()
    } else {
        forcePostAtFrontOfQueue(async, runnable)
    }
}

@JvmOverloads
fun forcePostAtFrontOfQueue(async: Boolean = true, runnable: Runnable) {
    val msg = obtainMessage(async, runnable)
    val result = msg.target.sendMessageAtFrontOfQueue(msg)
    check(result)
}

@SuppressLint("NewApi")
private fun obtainMessage(async: Boolean, runnable: Runnable) : Message {
    val msg = Message.obtain(MAIN_HANDLER, runnable)
    if (async && HAS_ASYNC) {
        msg.isAsynchronous = true
    }
    return msg
}
