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

package org.bubenheimer.android.log

import android.os.SystemClock

public class SparseLog constructor(private val period: Long = 60_000L) {
    private var lastLog = Long.MIN_VALUE

    public fun v(tag: String?, vararg args: Any?) {
        if (shouldLog()) {
            Log.v(tag, *args)
        }
    }

    public fun v(t: Throwable, tag: String?, vararg args: Any?) {
        if (shouldLog()) {
            Log.v(t, tag, *args)
        }
    }

    public fun d(tag: String?, vararg args: Any?) {
        if (shouldLog()) {
            Log.d(tag, *args)
        }
    }

    public fun d(t: Throwable, tag: String?, vararg args: Any?) {
        if (shouldLog()) {
            Log.d(t, tag, *args)
        }
    }

    public fun i(tag: String?, vararg args: Any?) {
        if (shouldLog()) {
            Log.i(tag, *args)
        }
    }

    public fun i(t: Throwable, tag: String?, vararg args: Any?) {
        if (shouldLog()) {
            Log.i(t, tag, *args)
        }
    }

    public fun w(tag: String?, vararg args: Any?) {
        if (shouldLog()) {
            Log.w(tag, *args)
        }
    }

    public fun w(t: Throwable, tag: String?, vararg args: Any?) {
        if (shouldLog()) {
            Log.w(t, tag, *args)
        }
    }

    public fun w(t: Throwable, tag: String?) {
        if (shouldLog()) {
            Log.w(t, tag)
        }
    }

    public fun e(tag: String?, vararg args: Any?) {
        if (shouldLog()) {
            Log.e(tag, *args)
        }
    }

    public fun e(t: Throwable, tag: String?, vararg args: Any?) {
        if (shouldLog()) {
            Log.e(t, tag, *args)
        }
    }

    public fun wtf(tag: String?, vararg args: Any?) {
        if (shouldLog()) {
            Log.wtf(tag, *args)
        }
    }

    public fun wtf(t: Throwable, tag: String?) {
        if (shouldLog()) {
            Log.wtf(t, tag)
        }
    }

    public fun wtf(t: Throwable, tag: String?, vararg args: Any?) {
        if (shouldLog()) {
            Log.wtf(t, tag, *args)
        }
    }

    private fun shouldLog(): Boolean {
        val time = SystemClock.elapsedRealtime()
        return if (lastLog + period < time) {
            lastLog = time
            true
        } else {
            false
        }
    }
}
