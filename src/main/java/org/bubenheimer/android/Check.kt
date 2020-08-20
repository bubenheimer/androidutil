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
package org.bubenheimer.android

import android.os.Handler
import android.os.Looper
import androidx.core.util.ObjectsCompat
import org.bubenheimer.android.threading.isCurrent

@Suppress("unused", "MemberVisibilityCanBePrivate")
public object Check {
    public fun notNull(obj: Any?) {
        if (obj == null) throw AssertionError()
    }

    public fun isNull(obj: Any?) {
        if (obj != null) throw AssertionError()
    }

    public fun equals(expected: Int, actual: Int) {
        if (expected != actual) throw AssertionError()
    }

    public fun equals(expected: Long, actual: Long) {
        if (expected != actual) throw AssertionError()
    }

    public fun equals(expected: Short, actual: Short) {
        if (expected != actual) throw AssertionError()
    }

    public fun equals(expected: Byte, actual: Byte) {
        if (expected != actual) throw AssertionError()
    }

    public fun equals(expected: Char, actual: Char) {
        if (expected != actual) throw AssertionError()
    }

    public fun equals(expected: Any, actual: Any?) {
        if (expected != actual) throw AssertionError(
            "Objects not equal -  expected: $expected  actual: $actual"
        )
    }

    public fun notEquals(expected: Int, actual: Int) {
        if (expected == actual) throw AssertionError()
    }

    public fun notEquals(expected: Long, actual: Long) {
        if (expected == actual) throw AssertionError()
    }

    public fun notEquals(expected: Short, actual: Short) {
        if (expected == actual) throw AssertionError()
    }

    public fun notEquals(expected: Byte, actual: Byte) {
        if (expected == actual) throw AssertionError()
    }

    public fun notEquals(expected: Char, actual: Char) {
        if (expected == actual) throw AssertionError()
    }

    public fun notEquals(expected: Any, actual: Any?) {
        if (ObjectsCompat.equals(expected, actual)) throw AssertionError()
    }

    public fun same(expected: Any, actual: Any) {
        if (expected !== actual) throw AssertionError(
            "Objects not the same -  expected: $expected  actual: $actual"
        )
    }

    public fun notSame(expected: Any, actual: Any?) {
        if (expected === actual) throw AssertionError()
    }

    public fun lessThan(value1: Int, value2: Int) {
        if (value1 >= value2) throw AssertionError()
    }

    public fun lessOrEqual(value1: Int, value2: Int) {
        if (value1 > value2) throw AssertionError()
    }

    public fun inRangeInclusive(value1: Int, value2: Int, value3: Int) {
        if (value1 > value2 || value2 > value3) throw AssertionError()
    }

    public fun inRangeExclusive(value1: Int, value2: Int, value3: Int) {
        if (value1 >= value2 || value2 >= value3) throw AssertionError()
    }

    public fun lessThan(value1: Long, value2: Long) {
        if (value1 >= value2) throw AssertionError()
    }

    public fun lessOrEqual(value1: Long, value2: Long) {
        if (value1 > value2) throw AssertionError()
    }

    public fun inRangeInclusive(value1: Long, value2: Long, value3: Long) {
        if (value1 > value2 || value2 > value3) throw AssertionError()
    }

    public fun inRangeExclusive(value1: Long, value2: Long, value3: Long) {
        if (value1 >= value2 || value2 >= value3) throw AssertionError()
    }

    public fun isOneOf(value: Int, vararg set: Int) {
        if (set.none { value == it }) throw AssertionError()
    }

    public fun notOneOf(value: Int, vararg set: Int) {
        if (set.any { value == it }) throw AssertionError()
    }

    public fun isOneOf(value: Long, vararg set: Long) {
        if (set.none { value == it }) throw AssertionError()
    }

    public fun notOneOf(value: Long, vararg set: Long) {
        if (set.any { value == it }) throw AssertionError()
    }

    public fun isOneOfObjects(value: Any?, vararg set: Any) {
        if (set.none { value == it }) throw AssertionError()
    }

    public fun notOneOfObjects(value: Any?, vararg set: Any) {
        if (set.any { value == it }) throw AssertionError()
    }

    public fun isTrue(value: Boolean) {
        isTrue("", value)
    }

    public fun isTrue(message: String, value: Boolean) {
        if (!value) throw AssertionError(message)
    }

    public fun isFalse(value: Boolean) {
        isFalse("", value)
    }

    public fun isFalse(message: String, value: Boolean) {
        if (value) throw AssertionError(message)
    }

    public fun fail(function: () -> Any?) {
        try {
            function()
        } catch (e: Throwable) {
            return
        }
        throw AssertionError()
    }

    public fun nofail(function: () -> Any?) {
        try {
            function()
        } catch (e: Error) {
            throw e
        } catch (e: RuntimeException) {
            throw e
        }
    }

    public fun fail() {
        throw AssertionError()
    }

    public fun onThread(thread: Thread) {
        if (!thread.isCurrent()) throw AssertionError()
    }

    public fun offThread(thread: Thread) {
        if (thread.isCurrent()) throw AssertionError()
    }

    public fun onLooperThread(looper: Looper) {
        onThread(looper.thread)
    }

    public fun offLooperThread(looper: Looper) {
        offThread(looper.thread)
    }

    public fun onHandlerThread(handler: Handler) {
        onLooperThread(handler.looper)
    }

    public fun offHandlerThread(handler: Handler) {
        offLooperThread(handler.looper)
    }

    public fun onMainThread() {
        onLooperThread(Looper.getMainLooper())
    }

    public fun onWorkerThread() {
        offLooperThread(Looper.getMainLooper())
    }
}
