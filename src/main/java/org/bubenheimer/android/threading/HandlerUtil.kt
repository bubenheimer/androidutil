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

import android.os.Build
import android.os.Handler
import android.os.Looper
import org.bubenheimer.android.log.Log
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException

public object HandlerUtil {
    private val TAG = HandlerUtil::class.java.simpleName

    private val constructor: Constructor<Handler>? by lazy(LazyThreadSafetyMode.PUBLICATION) {
        try {
            Handler::class.java.getConstructor(
                Looper::class.java, Handler.Callback::class.java, java.lang.Boolean.TYPE
            )
        } catch (e: NoSuchMethodException) {
            Log.wx(e, TAG)
            null
        }
    }

    public fun createAsync(
        looper: Looper = wrapLooper(null),
        callback: Handler.Callback? = null
    ): Handler =
        if (Build.VERSION_CODES.P <= Build.VERSION.SDK_INT) {
            if (callback == null) Handler.createAsync(looper)
            else Handler.createAsync(looper, callback)
        } else {
            createReflectiveAsync(looper, callback)
        }

    private fun wrapLooper(@Suppress("SameParameterValue") looper: Looper?): Looper =
        looper
            ?: Looper.myLooper()
            ?: throw RuntimeException(
                "Can't create handler inside thread" +
                        " ${Thread.currentThread()} that has not called Looper.prepare()"
            )

    private fun createReflectiveAsync(looper: Looper, callback: Handler.Callback?): Handler {
        constructor?.let {
            try {
                return it.newInstance(looper, callback, true)
            } catch (e: Exception) {
                when (e) {
                    is IllegalAccessException,
                    is InstantiationException,
                    is InvocationTargetException
                    -> Log.wx(e, TAG)
                    else -> throw e
                }
            }
        }
        return Handler(looper, callback)
    }
}
