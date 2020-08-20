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

public object Log {
    public interface CrashLog {
        public fun crashLog(priority: Int, tag: String?, msg: String?)
        public fun crashLog(priority: Int, t: Throwable?, tag: String?, msg: String?)
    }

    private val TAG = Log::class.simpleName!!

    public const val VERBOSE: Int = android.util.Log.VERBOSE
    public const val DEBUG: Int = android.util.Log.DEBUG
    public const val INFO: Int = android.util.Log.INFO
    public const val WARN: Int = android.util.Log.WARN
    public const val ERROR: Int = android.util.Log.ERROR
    public const val ASSERT: Int = android.util.Log.ASSERT

    private fun toString(vararg args: Any?) = when {
        args.isEmpty() -> ""

        //Common special case
        args.size == 1 -> args[0].toString()

        else -> buildString { args.forEach { append(it) } }
    }

    public var crashLog: CrashLog? = null
        set(value) {
            field = value
            w(TAG, "Crash log reset")
        }

    public fun v(tag: String?, vararg args: Any?): Unit = println(VERBOSE, tag, *args)

    public fun v(t: Throwable?, tag: String?, vararg args: Any?): Unit =
        println(VERBOSE, t, tag, *args)

    public fun d(tag: String?, vararg args: Any?): Unit = println(DEBUG, tag, *args)

    public fun d(t: Throwable?, tag: String?, vararg args: Any?): Unit = println(DEBUG, t, tag, *args)

    public fun dx(t: Throwable?, tag: String?): Unit = println(DEBUG, t, tag, t?.message)

    public fun i(tag: String?, vararg args: Any?): Unit = println(INFO, tag, *args)

    public fun i(t: Throwable?, tag: String?, vararg args: Any?): Unit = println(INFO, t, tag, *args)

    public fun ix(t: Throwable?, tag: String?): Unit = println(INFO, t, tag, t?.message)

    public fun w(tag: String?, vararg args: Any?): Unit = println(WARN, tag, *args)

    public fun w(t: Throwable?, tag: String?, vararg args: Any?): Unit = println(WARN, t, tag, *args)

    public fun w(t: Throwable?, tag: String?): Unit = println(WARN, t, tag)

    public fun wx(t: Throwable?, tag: String?): Unit = println(WARN, t, tag, t?.message)

    public fun e(tag: String?, vararg args: Any?): Unit = println(ERROR, tag, *args)

    public fun e(t: Throwable?, tag: String?, vararg args: Any?): Unit = println(ERROR, t, tag, *args)

    public fun ex(t: Throwable?, tag: String?): Unit = println(ERROR, t, tag, t?.message)

    public fun wtf(tag: String?, vararg args: Any?) {
        val msg = toString(*args)
        if (!crashLog(ASSERT, tag, msg)) {
            android.util.Log.wtf(tag, msg)
        }
    }

    public fun wtf(t: Throwable, tag: String?) {
        if (!crashLog(ASSERT, t, tag, "")) {
            android.util.Log.wtf(tag, t)
        }
    }

    public fun wtf(t: Throwable?, tag: String?, vararg args: Any?) {
        val msg = toString(*args)
        if (!crashLog(ASSERT, t, tag, msg)) {
            android.util.Log.wtf(tag, msg, t)
        }
    }

    public fun println(priority: Int, tag: String?, vararg args: Any?) {
        val msg = toString(*args)
        if (priority == VERBOSE || !crashLog(priority, tag, msg)) {
            android.util.Log.println(priority, tag, msg)
        }
    }

    public fun println(priority: Int, t: Throwable?, tag: String?, vararg args: Any?) {
        val msg = toString(*args)
        android.util.Log.println(priority, tag, "$msg\n${android.util.Log.getStackTraceString(t)}")
        if (priority != VERBOSE) {
            crashLog(priority, t, tag, msg)
        }
    }

    public fun getStackTraceString(t: Throwable?): String {
        return android.util.Log.getStackTraceString(t)
    }

    private fun crashLog(priority: Int, tag: String?, msg: String?): Boolean {
        crashLog?.crashLog(priority, tag, msg)
        return crashLog != null
    }

    private fun crashLog(priority: Int, t: Throwable?, tag: String?, msg: String?): Boolean {
        crashLog?.crashLog(priority, t, tag, msg)
        return crashLog != null
    }
}
