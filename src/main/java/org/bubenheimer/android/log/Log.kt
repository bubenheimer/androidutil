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

import org.bubenheimer.android.log.Log.Priority.ASSERT
import org.bubenheimer.android.log.Log.Priority.DEBUG
import org.bubenheimer.android.log.Log.Priority.ERROR
import org.bubenheimer.android.log.Log.Priority.INFO
import org.bubenheimer.android.log.Log.Priority.VERBOSE
import org.bubenheimer.android.log.Log.Priority.WARN
import android.util.Log as FrameworkLog

public object Log {
    public interface CrashLog {
        public fun crashLog(
            priority: Priority,
            t: Throwable?,
            tag: String?,
            msg: String?
        )
    }

    private val TAG = Log::class.java.simpleName

    public enum class Priority(public val priority: Int) {
        VERBOSE(FrameworkLog.VERBOSE),
        DEBUG(FrameworkLog.DEBUG),
        INFO(FrameworkLog.INFO),
        WARN(FrameworkLog.WARN),
        ERROR(FrameworkLog.ERROR),
        ASSERT(FrameworkLog.ASSERT)
    }

    public var crashLog: CrashLog? = null
        set(value) {
            field = value
            w(TAG, "Crash log reset")
        }

    public fun v(tag: String?, text: String): Unit =
        println(VERBOSE, tag, text) { FrameworkLog.v(tag, text) }

    public fun v(t: Throwable?, tag: String?, text: String): Unit =
        println(VERBOSE, t, tag, text) { FrameworkLog.v(tag, text, t) }

    public fun v(t: Throwable?, tag: String?): Unit =
        println(VERBOSE, t, tag) { FrameworkLog.v(tag, "", t) }

    public fun d(tag: String?, text: String): Unit =
        println(DEBUG, tag, text) { FrameworkLog.d(tag, text) }

    public fun d(t: Throwable?, tag: String?, text: String): Unit =
        println(DEBUG, t, tag, text) { FrameworkLog.d(tag, text, t) }

    public fun d(t: Throwable?, tag: String?): Unit =
        println(DEBUG, t, tag) { FrameworkLog.d(tag, "", t) }

    public fun dx(t: Throwable?, tag: String?): Unit =
        println(DEBUG, t, tag, t?.message) { FrameworkLog.d(tag, "", t) }

    public fun i(tag: String?, text: String): Unit =
        println(INFO, tag, text) { FrameworkLog.i(tag, text) }

    public fun i(t: Throwable?, tag: String?, text: String): Unit =
        println(INFO, t, tag, text) { FrameworkLog.i(tag, text, t) }

    public fun i(t: Throwable?, tag: String?): Unit =
        println(INFO, t, tag) { FrameworkLog.i(tag, "", t) }

    public fun ix(t: Throwable?, tag: String?): Unit =
        println(INFO, t, tag, t?.message) { FrameworkLog.i(tag, "", t) }

    public fun w(tag: String?, text: String): Unit =
        println(WARN, tag, text) { FrameworkLog.w(tag, text) }

    public fun w(t: Throwable?, tag: String?, text: String): Unit =
        println(WARN, t, tag, text) { FrameworkLog.w(tag, text, t) }

    public fun w(t: Throwable?, tag: String?): Unit =
        println(WARN, t, tag) { FrameworkLog.w(tag, t) }

    public fun wx(t: Throwable?, tag: String?): Unit =
        println(WARN, t, tag, t?.message) { FrameworkLog.w(tag, t) }

    public fun e(tag: String?, text: String): Unit =
        println(ERROR, tag, text) { FrameworkLog.e(tag, text) }

    public fun e(t: Throwable?, tag: String?, text: String): Unit =
        println(ERROR, t, tag, text) { FrameworkLog.e(tag, text, t) }

    public fun e(t: Throwable?, tag: String?): Unit =
        println(ERROR, t, tag) { FrameworkLog.e(tag, "", t) }

    public fun ex(t: Throwable?, tag: String?): Unit =
        println(ERROR, t, tag, t?.message) { FrameworkLog.e(tag, "", t) }

    public fun wtf(tag: String?, text: String): Unit =
        println(ASSERT, tag, text) { FrameworkLog.wtf(tag, text) }

    public fun wtf(t: Throwable, tag: String?): Unit =
        println(ASSERT, t, tag) { FrameworkLog.wtf(tag, "", t) }

    public fun wtf(t: Throwable?, tag: String?, text: String): Unit =
        println(ASSERT, t, tag, text) { FrameworkLog.wtf(tag, text, t) }

    public fun wtfx(t: Throwable?, tag: String?): Unit =
        println(ASSERT, t, tag, t?.message) { FrameworkLog.wtf(tag, "", t) }

    public fun println(priority: Priority, tag: String?, text: String) {
        println(priority, tag, text) { FrameworkLog.println(priority.priority, tag, text) }
    }

    private inline fun println(
        priority: Priority,
        tag: String?,
        text: String? = null,
        frameworkLog: () -> Int
    ): Unit = println(priority, t = null, tag = tag, text = text, frameworkLog)

    private inline fun println(
        priority: Priority,
        t: Throwable?,
        tag: String?,
        text: String? = null,
        frameworkLog: () -> Int
    ) {
        frameworkLog()

        if (priority != VERBOSE) {
            crashLog(
                priority = priority,
                t = t,
                tag = tag,
                msg = text
            )
        }
    }

    public fun getStackTraceString(t: Throwable?): String {
        return FrameworkLog.getStackTraceString(t)
    }

    private fun crashLog(
        priority: Priority,
        t: Throwable?,
        tag: String?,
        msg: String?
    ) {
        crashLog?.crashLog(priority, t, tag, msg)
    }
}
