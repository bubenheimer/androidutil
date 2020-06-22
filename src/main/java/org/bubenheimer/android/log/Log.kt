/*
 * Copyright (c) 2015-2020 Uli Bubenheimer
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

package org.bubenheimer.android.log

object Log {
    interface CrashLog {
        fun crashLog(priority: Int, tag: String?, msg: String?)
        fun crashLog(priority: Int, t: Throwable?, tag: String?, msg: String?)
    }

    private val TAG = Log::class.simpleName!!

    const val VERBOSE = android.util.Log.VERBOSE
    const val DEBUG = android.util.Log.DEBUG
    const val INFO = android.util.Log.INFO
    const val WARN = android.util.Log.WARN
    const val ERROR = android.util.Log.ERROR
    const val ASSERT = android.util.Log.ASSERT

    private fun toString(vararg args: Any?) = when {
        args.isEmpty() -> ""

        //Common special case
        args.size == 1 -> args[0].toString()

        else -> buildString { args.forEach { append(it) } }
    }

    @set:JvmStatic
    var crashLog: CrashLog? = null
        set(value) {
            field = value
            w(TAG, "Crash log reset")
        }

    @JvmStatic fun v(tag: String?, vararg args: Any?) = println(VERBOSE, tag, *args)

    @JvmStatic fun v(t: Throwable?, tag: String?, vararg args: Any?) =
            println(VERBOSE, t, tag, *args)

    @JvmStatic fun d(tag: String?, vararg args: Any?) = println(DEBUG, tag, *args)

    @JvmStatic fun d(t: Throwable?, tag: String?, vararg args: Any?) = println(DEBUG, t, tag, *args)

    @JvmStatic fun dx(t: Throwable?, tag: String?) = println(DEBUG, t, tag, t?.message)

    @JvmStatic fun i(tag: String?, vararg args: Any?) = println(INFO, tag, *args)

    @JvmStatic fun i(t: Throwable?, tag: String?, vararg args: Any?) = println(INFO, t, tag, *args)

    @JvmStatic fun ix(t: Throwable?, tag: String?) = println(INFO, t, tag, t?.message)

    @JvmStatic fun w(tag: String?, vararg args: Any?) = println(WARN, tag, *args)

    @JvmStatic fun w(t: Throwable?, tag: String?, vararg args: Any?) = println(WARN, t, tag, *args)

    @JvmStatic fun w(t: Throwable?, tag: String?) = println(WARN, t, tag)

    @JvmStatic fun wx(t: Throwable?, tag: String?) = println(WARN, t, tag, t?.message)

    @JvmStatic fun e(tag: String?, vararg args: Any?) = println(ERROR, tag, *args)

    @JvmStatic fun e(t: Throwable?, tag: String?, vararg args: Any?) = println(ERROR, t, tag, *args)

    @JvmStatic fun ex(t: Throwable?, tag: String?) = println(ERROR, t, tag, t?.message)

    @JvmStatic fun wtf(tag: String?, vararg args: Any?) {
        val msg = toString(*args)
        if (!crashLog(ASSERT, tag, msg)) {
            android.util.Log.wtf(tag, msg)
        }
    }

    @JvmStatic fun wtf(t: Throwable, tag: String?) {
        if (!crashLog(ASSERT, t, tag, "")) {
            android.util.Log.wtf(tag, t)
        }
    }

    @JvmStatic fun wtf(t: Throwable?, tag: String?, vararg args: Any?) {
        val msg = toString(*args)
        if (!crashLog(ASSERT, t, tag, msg)) {
            android.util.Log.wtf(tag, msg, t)
        }
    }

    @JvmStatic fun println(priority: Int, tag: String?, vararg args: Any?) {
        val msg = toString(*args)
        if (priority == VERBOSE || !crashLog(priority, tag, msg)) {
            android.util.Log.println(priority, tag, msg)
        }
    }

    @JvmStatic fun println(priority: Int, t: Throwable?, tag: String?, vararg args: Any?) {
        val msg = toString(*args)
        android.util.Log.println(priority, tag, "$msg\n${android.util.Log.getStackTraceString(t)}")
        if (priority != VERBOSE) {
            crashLog(priority, t, tag, msg)
        }
    }

    @JvmStatic fun getStackTraceString(t: Throwable?): String {
        return android.util.Log.getStackTraceString(t)
    }

    private fun crashLog(priority: Int, tag: String?, msg: String?) : Boolean {
        crashLog?.crashLog(priority, tag, msg)
        return crashLog != null
    }

    private fun crashLog(priority: Int, t: Throwable?, tag: String?, msg: String?) : Boolean {
        crashLog?.crashLog(priority, t, tag, msg)
        return crashLog != null
    }
}
