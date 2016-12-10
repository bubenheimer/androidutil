/*
 * Copyright (c) 2015-2016 Uli Bubenheimer.
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

package org.bubenheimer.android.log;

import android.os.SystemClock;

public final class SparseLog {
    private final long period;
    private long lastLog = Long.MIN_VALUE;

    public SparseLog() {
        this(60_000L);
    }

    public SparseLog(final long period) {
        this.period = period;
    }

    public void v(final String tag, final Object... args) {
        if (shouldLog()) {
            Log.v(tag, args);
        }
    }

    public void v(final Throwable t, final String tag, final Object... args) {
        if (shouldLog()) {
            Log.v(t, tag, args);
        }
    }

    public void d(final String tag, final Object... args) {
        if (shouldLog()) {
            Log.d(tag, args);
        }
    }

    public void d(final Throwable t, final String tag, final Object... args) {
        if (shouldLog()) {
            Log.d(t, tag, args);
        }
    }

    public void i(final String tag, final Object... args) {
        if (shouldLog()) {
            Log.i(tag, args);
        }
    }

    public void i(final Throwable t, final String tag, final Object... args) {
        if (shouldLog()) {
            Log.i(t, tag, args);
        }
    }

    public void w(final String tag, final Object... args) {
        if (shouldLog()) {
            Log.w(tag, args);
        }
    }

    public void w(final Throwable t, final String tag, final Object... args) {
        if (shouldLog()) {
            Log.w(t, tag, args);
        }
    }

    public void w(final Throwable t, final String tag) {
        if (shouldLog()) {
            Log.w(t, tag);
        }
    }

    public void e(final String tag, final Object... args) {
        if (shouldLog()) {
            Log.e(tag, args);
        }
    }

    public void e(final Throwable t, final String tag, final Object... args) {
        if (shouldLog()) {
            Log.e(t, tag, args);
        }
    }

    public void wtf(final String tag, final Object... args) {
        if (shouldLog()) {
            Log.wtf(tag, args);
        }
    }

    public void wtf(final Throwable t, final String tag) {
        if (shouldLog()) {
            Log.wtf(t, tag);
        }
    }

    public void wtf(final Throwable t, final String tag, final Object... args) {
        if (shouldLog()) {
            Log.wtf(t, tag, args);
        }
    }

    private boolean shouldLog() {
        final long time = SystemClock.elapsedRealtime();
        if (time > lastLog + period) {
            lastLog = time;
            return true;
        } else {
            return false;
        }
    }
}
