/*
 * Copyright (c) 2015-2017 Uli Bubenheimer.
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

import org.bubenheimer.util.Uninstantiable;

public final class Log extends Uninstantiable {
    private static final String TAG = Log.class.getSimpleName();

    public interface CrashLog {
        void crashLog(int priority, String tag, String msg);
        void crashLog(int priority, Throwable t, String tag, String msg);
    }

    public static final int VERBOSE =   android.util.Log.VERBOSE;
    public static final int DEBUG =     android.util.Log.DEBUG;
    public static final int INFO =      android.util.Log.INFO;
    public static final int WARN =      android.util.Log.WARN;
    public static final int ERROR =     android.util.Log.ERROR;
    public static final int ASSERT =    android.util.Log.ASSERT;

    private static String toString(final Object... args) {
        if (args == null || args.length == 0) {
            return "";
        }
        //Common special case
        if (args.length == 1) {
            final Object arg = args[0];
            return arg == null ? "null" : arg.toString();
        }
        StringBuilder builder = new StringBuilder();
        for (Object obj : args) {
            builder.append(obj);
        }
        return builder.toString();
    }

    private static CrashLog crashLog;

    public static void setCrashLog(final CrashLog crashLog) {
        final boolean reset = crashLog != null;
        Log.crashLog = crashLog;
        w(TAG, "Crash log reset");
    }

    public static void v(final String tag, final Object... args) {
        println(VERBOSE, tag, args);
    }

    public static void v(final Throwable t, final String tag, final Object... args) {
        println(VERBOSE, t, tag, args);
    }

    public static void d(final String tag, final Object... args) {
        println(DEBUG, tag, args);
    }

    public static void d(final Throwable t, final String tag, final Object... args) {
        println(DEBUG, t, tag, args);
    }

    public static void dx(final Throwable t, final String tag) {
        println(DEBUG, t, tag, t.getMessage());
    }

    public static void i(final String tag, final Object... args) {
        println(INFO, tag, args);
    }

    public static void i(final Throwable t, final String tag, final Object... args) {
        println(INFO, t, tag, args);
    }

    public static void ix(final Throwable t, final String tag) {
        println(INFO, t, tag, t.getMessage());
    }

    public static void w(final String tag, final Object... args) {
        println(WARN, tag, args);
    }

    public static void w(final Throwable t, final String tag, final Object... args) {
        println(WARN, t, tag, args);
    }

    public static void w(final Throwable t, final String tag) {
        println(WARN, t, tag);
    }

    public static void wx(final Throwable t, final String tag) {
        println(WARN, t, tag, t.getMessage());
    }

    public static void e(final String tag, final Object... args) {
        println(ERROR, tag, args);
    }

    public static void e(final Throwable t, final String tag, final Object... args) {
        println(ERROR, t, tag, args);
    }

    public static void ex(final Throwable t, final String tag) {
        println(ERROR, t, tag, t.getMessage());
    }

    public static void wtf(final String tag, final Object... args) {
        final String msg = toString(args);
        android.util.Log.wtf(tag, msg);
        crashLog(ASSERT, tag, msg);
    }

    public static void wtf(final Throwable t, final String tag) {
        android.util.Log.wtf(tag, t);
        crashLog(ASSERT, t, tag, "");
    }

    public static void wtf(final Throwable t, final String tag, final Object... args) {
        final String msg = toString(args);
        android.util.Log.wtf(tag, msg, t);
        crashLog(ASSERT, t, tag, msg);
    }

    public static void println(final int priority, final String tag, final Object... args) {
        final String msg = toString(args);
        android.util.Log.println(priority, tag, msg);
        if (priority != VERBOSE) {
            crashLog(priority, tag, msg);
        }
    }

    public static void println(final int priority, final Throwable t, final String tag,
                               final Object... args) {
        final String msg = toString(args);
        android.util.Log.println(priority, tag, msg + '\n'
                + android.util.Log.getStackTraceString(t));
        if (priority != VERBOSE) {
            crashLog(priority, t, tag, msg);
        }
    }

    public static String getStackTraceString(final Throwable t) {
        return android.util.Log.getStackTraceString(t);
    }

    private static void crashLog(final int priority, final String tag, final String msg) {
        if (crashLog != null) {
            crashLog.crashLog(priority, tag, msg);
        }
    }

    private static void crashLog(final int priority, final Throwable t, final String tag,
                                 final String msg) {
        if (crashLog != null) {
            crashLog.crashLog(priority, t, tag, msg);
        }
    }
}
