/*
 * Copyright (c) 2015-2019 Uli Bubenheimer. All rights reserved.
 */

package org.bubenheimer.android.threading

import android.os.Build
import android.os.Handler
import android.os.Looper
import org.bubenheimer.android.log.Log
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException

object HandlerUtil {
    private val TAG = HandlerUtil::class.java.simpleName

    private val constructor: Constructor<Handler>? by lazy {
        try {
            Handler::class.java.getConstructor(
                    Looper::class.java, Handler.Callback::class.java, java.lang.Boolean.TYPE)
        } catch (e: NoSuchMethodException) {
            Log.wx(e, TAG)
            null
        }
    }

    @JvmOverloads
    fun createAsync(
            looper: Looper? = null,
            callback: Handler.Callback? = null): Handler {
        return if (Build.VERSION_CODES.P <= Build.VERSION.SDK_INT) {
            if (callback == null) {
                Handler.createAsync(wrapLooper(looper))
            } else {
                Handler.createAsync(wrapLooper(looper), callback)
            }
        } else {
            createReflectiveAsync(looper, callback)
        }
    }

    private fun wrapLooper(looper: Looper?): Looper {
        return looper
                ?: Looper.myLooper()
                ?: throw RuntimeException(
                        "Can't create handler inside thread " + Thread.currentThread()
                                + " that has not called Looper.prepare()")
    }

    private fun createReflectiveAsync(
            looper: Looper?,
            callback: Handler.Callback?): Handler {
        constructor?.let {
            try {
                return@createReflectiveAsync it.newInstance(looper, callback, true)
            } catch (e: IllegalAccessException) {
                Log.ex(e, TAG)
            } catch (e: InstantiationException) {
                Log.ex(e, TAG)
            } catch (e: InvocationTargetException) {
                Log.ex(e, TAG)
            }
        }
        return Handler(looper, callback)
    }
}
