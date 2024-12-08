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
        public fun crashLog(priority: Priority, tag: String?, msg: String?)
        public fun crashLog(priority: Priority, t: Throwable?, tag: String?, msg: String?)
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

    public fun v(tag: String?, text: String): Unit = println(VERBOSE, tag, text)

    public fun v(t: Throwable?, tag: String?, text: String): Unit =
        println(VERBOSE, t, tag, text)

    public fun v(t: Throwable?, tag: String?): Unit = println(VERBOSE, t, tag)

    public fun d(tag: String?, text: String): Unit = println(DEBUG, tag, text)

    public fun d(t: Throwable?, tag: String?, text: String): Unit = println(DEBUG, t, tag, text)

    public fun d(t: Throwable?, tag: String?): Unit = println(DEBUG, t, tag)

    public fun dx(t: Throwable?, tag: String?): Unit = println(DEBUG, t, tag, t?.message)

    public fun i(tag: String?, text: String): Unit = println(INFO, tag, text)

    public fun i(t: Throwable?, tag: String?, text: String): Unit = println(INFO, t, tag, text)

    public fun i(t: Throwable?, tag: String?): Unit = println(INFO, t, tag)

    public fun ix(t: Throwable?, tag: String?): Unit = println(INFO, t, tag, t?.message)

    public fun w(tag: String?, text: String): Unit = println(WARN, tag, text)

    public fun w(t: Throwable?, tag: String?, text: String): Unit = println(WARN, t, tag, text)

    public fun w(t: Throwable?, tag: String?): Unit = println(WARN, t, tag)

    public fun wx(t: Throwable?, tag: String?): Unit = println(WARN, t, tag, t?.message)

    public fun e(tag: String?, text: String): Unit = println(ERROR, tag, text)

    public fun e(t: Throwable?, tag: String?, text: String): Unit = println(ERROR, t, tag, text)

    public fun e(t: Throwable?, tag: String?): Unit = println(ERROR, t, tag)

    public fun ex(t: Throwable?, tag: String?): Unit = println(ERROR, t, tag, t?.message)

    public fun wtf(tag: String?, text: String) {
        if (!crashLog(ASSERT, tag, text)) {
            FrameworkLog.wtf(tag, text)
        }
    }

    public fun wtf(t: Throwable, tag: String?) {
        if (!crashLog(ASSERT, t, tag, "")) {
            FrameworkLog.wtf(tag, t)
        }
    }

    public fun wtf(t: Throwable?, tag: String?, text: String) {
        if (!crashLog(ASSERT, t, tag, text)) {
            FrameworkLog.wtf(tag, text, t)
        }
    }

    public fun println(priority: Priority, tag: String?, text: String? = null) {
        val msg = text ?: ""
        if (priority == VERBOSE || !crashLog(priority, tag, msg)) {
            FrameworkLog.println(priority.priority, tag, msg)
        }
    }

    @Suppress("LogConditional")
    public fun println(priority: Priority, t: Throwable?, tag: String?, text: String? = null) {
        val msg = text ?: ""
        FrameworkLog.println(priority.priority, tag, "$msg\n${FrameworkLog.getStackTraceString(t)}")
        if (priority != VERBOSE) {
            crashLog(priority, t, tag, msg)
        }
    }

    public fun getStackTraceString(t: Throwable?): String {
        return FrameworkLog.getStackTraceString(t)
    }

    private fun crashLog(priority: Priority, tag: String?, msg: String?): Boolean {
        crashLog?.crashLog(priority, tag, msg)
        return crashLog != null
    }

    private fun crashLog(priority: Priority, t: Throwable?, tag: String?, msg: String?): Boolean {
        crashLog?.crashLog(priority, t, tag, msg)
        return crashLog != null
    }
}
