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

    public fun v(tag: String?, text: String) {
        if (shouldLog()) {
            Log.v(tag, text)
        }
    }

    public fun v(t: Throwable, tag: String?, text: String) {
        if (shouldLog()) {
            Log.v(t, tag, text)
        }
    }

    public fun d(tag: String?, text: String) {
        if (shouldLog()) {
            Log.d(tag, text)
        }
    }

    public fun d(t: Throwable, tag: String?, text: String) {
        if (shouldLog()) {
            Log.d(t, tag, text)
        }
    }

    public fun i(tag: String?, text: String) {
        if (shouldLog()) {
            Log.i(tag, text)
        }
    }

    public fun i(t: Throwable, tag: String?, text: String) {
        if (shouldLog()) {
            Log.i(t, tag, text)
        }
    }

    public fun w(tag: String?, text: String) {
        if (shouldLog()) {
            Log.w(tag, text)
        }
    }

    public fun w(t: Throwable, tag: String?, text: String) {
        if (shouldLog()) {
            Log.w(t, tag, text)
        }
    }

    public fun w(t: Throwable, tag: String?) {
        if (shouldLog()) {
            Log.w(t, tag)
        }
    }

    public fun e(tag: String?, text: String) {
        if (shouldLog()) {
            Log.e(tag, text)
        }
    }

    public fun e(t: Throwable, tag: String?, text: String) {
        if (shouldLog()) {
            Log.e(t, tag, text)
        }
    }

    public fun wtf(tag: String?, text: String) {
        if (shouldLog()) {
            Log.wtf(tag, text)
        }
    }

    public fun wtf(t: Throwable, tag: String?) {
        if (shouldLog()) {
            Log.wtf(t, tag)
        }
    }

    public fun wtf(t: Throwable, tag: String?, text: String) {
        if (shouldLog()) {
            Log.wtf(t, tag, text)
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
