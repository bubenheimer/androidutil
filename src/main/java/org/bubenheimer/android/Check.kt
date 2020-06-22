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
object Check {
    @JvmStatic
    fun notNull(obj: Any?) {
        if (obj == null) throw AssertionError()
    }

    @JvmStatic
    fun isNull(obj: Any?) {
        if (obj != null) throw AssertionError()
    }

    @JvmStatic
    fun equals(expected: Int, actual: Int) {
        if (expected != actual) throw AssertionError()
    }

    @JvmStatic
    fun equals(expected: Long, actual: Long) {
        if (expected != actual) throw AssertionError()
    }

    @JvmStatic
    fun equals(expected: Short, actual: Short) {
        if (expected != actual) throw AssertionError()
    }

    @JvmStatic
    fun equals(expected: Byte, actual: Byte) {
        if (expected != actual) throw AssertionError()
    }

    @JvmStatic
    fun equals(expected: Char, actual: Char) {
        if (expected != actual) throw AssertionError()
    }

    @JvmStatic
    fun equals(expected: Any, actual: Any?) {
        if (expected != actual) throw AssertionError(
                "Objects not equal -  expected: $expected  actual: $actual")
    }

    @JvmStatic
    fun notEquals(expected: Int, actual: Int) {
        if (expected == actual) throw AssertionError()
    }

    @JvmStatic
    fun notEquals(expected: Long, actual: Long) {
        if (expected == actual) throw AssertionError()
    }

    @JvmStatic
    fun notEquals(expected: Short, actual: Short) {
        if (expected == actual) throw AssertionError()
    }

    @JvmStatic
    fun notEquals(expected: Byte, actual: Byte) {
        if (expected == actual) throw AssertionError()
    }

    @JvmStatic
    fun notEquals(expected: Char, actual: Char) {
        if (expected == actual) throw AssertionError()
    }

    @JvmStatic
    fun notEquals(expected: Any, actual: Any?) {
        if (ObjectsCompat.equals(expected, actual)) throw AssertionError()
    }

    @JvmStatic
    fun same(expected: Any, actual: Any) {
        if (expected !== actual) throw AssertionError(
                "Objects not the same -  expected: $expected  actual: $actual")
    }

    @JvmStatic
    fun notSame(expected: Any, actual: Any?) {
        if (expected === actual) throw AssertionError()
    }

    @JvmStatic
    fun lessThan(value1: Int, value2: Int) {
        if (value1 >= value2) throw AssertionError()
    }

    @JvmStatic
    fun lessOrEqual(value1: Int, value2: Int) {
        if (value1 > value2) throw AssertionError()
    }

    @JvmStatic
    fun inRangeInclusive(value1: Int, value2: Int, value3: Int) {
        if (value1 > value2 || value2 > value3) throw AssertionError()
    }

    @JvmStatic
    fun inRangeExclusive(value1: Int, value2: Int, value3: Int) {
        if (value1 >= value2 || value2 >= value3) throw AssertionError()
    }

    @JvmStatic
    fun lessThan(value1: Long, value2: Long) {
        if (value1 >= value2) throw AssertionError()
    }

    @JvmStatic
    fun lessOrEqual(value1: Long, value2: Long) {
        if (value1 > value2) throw AssertionError()
    }

    @JvmStatic
    fun inRangeInclusive(value1: Long, value2: Long, value3: Long) {
        if (value1 > value2 || value2 > value3) throw AssertionError()
    }

    @JvmStatic
    fun inRangeExclusive(value1: Long, value2: Long, value3: Long) {
        if (value1 >= value2 || value2 >= value3) throw AssertionError()
    }

    @JvmStatic
    fun isOneOf(value: Int, vararg set: Int) {
        if (set.none { value == it }) throw AssertionError()
    }

    @JvmStatic
    fun notOneOf(value: Int, vararg set: Int) {
        if (set.any { value == it }) throw AssertionError()
    }

    @JvmStatic
    fun isOneOf(value: Long, vararg set: Long) {
        if (set.none { value == it }) throw AssertionError()
    }

    @JvmStatic
    fun notOneOf(value: Long, vararg set: Long) {
        if (set.any { value == it }) throw AssertionError()
    }

    @JvmStatic
    fun isOneOfObjects(value: Any?, vararg set: Any) {
        if (set.none { value == it }) throw AssertionError()
    }

    @JvmStatic
    fun notOneOfObjects(value: Any?, vararg set: Any) {
        if (set.any { value == it }) throw AssertionError()
    }

    @JvmStatic
    fun isTrue(value: Boolean) {
        isTrue("", value)
    }

    @JvmStatic
    fun isTrue(message: String, value: Boolean) {
        if (!value) throw AssertionError(message)
    }

    @JvmStatic
    fun isFalse(value: Boolean) {
        isFalse("", value)
    }

    @JvmStatic
    fun isFalse(message: String, value: Boolean) {
        if (value) throw AssertionError(message)
    }

    @JvmStatic
    fun fail(function: () -> Any?) {
        try {
            function()
        } catch (e: Throwable) {
            return
        }
        throw AssertionError()
    }

    @JvmStatic
    fun nofail(function: () -> Any?) {
        try {
            function()
        } catch (e: Error) {
            throw e
        } catch (e: RuntimeException) {
            throw e
        }
    }

    @JvmStatic
    fun fail() {
        throw AssertionError()
    }

    @JvmStatic
    fun onThread(thread: Thread) {
        if (!thread.isCurrent()) throw AssertionError()
    }

    @JvmStatic
    fun offThread(thread: Thread) {
        if (thread.isCurrent()) throw AssertionError()
    }

    @JvmStatic
    fun onLooperThread(looper: Looper) {
        onThread(looper.thread)
    }

    @JvmStatic
    fun offLooperThread(looper: Looper) {
        offThread(looper.thread)
    }

    @JvmStatic
    fun onHandlerThread(handler: Handler) {
        onLooperThread(handler.looper)
    }

    @JvmStatic
    fun offHandlerThread(handler: Handler) {
        offLooperThread(handler.looper)
    }

    @JvmStatic
    fun onMainThread() {
        onLooperThread(Looper.getMainLooper())
    }

    @JvmStatic
    fun onWorkerThread() {
        offLooperThread(Looper.getMainLooper())
    }
}
