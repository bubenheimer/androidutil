/*
 * Copyright (c) 2015-2019 Uli Bubenheimer. All rights reserved.
 */

package org.bubenheimer.android.threading

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import org.bubenheimer.android.log.Log
import java.util.concurrent.Executor

class LooperExecutor
@JvmOverloads constructor(looper: Looper, async: Boolean = true) : Executor {

    private val handler = Handler(looper)
    private val async = async && HAS_ASYNC

    @SuppressLint("NewApi")
    override fun execute(command: Runnable) {
        val msg = Message.obtain(handler, command)
        if (async) {
            msg.isAsynchronous = async
        }
        msg.sendToTarget()
    }

    companion object {
        private val TAG = LooperExecutor::class.java.simpleName

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
    }
}
