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
    fun notNull(obj: Any?) {
        if (obj == null) throw AssertionError()
    }

    fun isNull(obj: Any?) {
        if (obj != null) throw AssertionError()
    }

    fun equals(expected: Int, actual: Int) {
        if (expected != actual) throw AssertionError()
    }

    fun equals(expected: Long, actual: Long) {
        if (expected != actual) throw AssertionError()
    }

    fun equals(expected: Short, actual: Short) {
        if (expected != actual) throw AssertionError()
    }

    fun equals(expected: Byte, actual: Byte) {
        if (expected != actual) throw AssertionError()
    }

    fun equals(expected: Char, actual: Char) {
        if (expected != actual) throw AssertionError()
    }

    fun equals(expected: Any, actual: Any?) {
        if (expected != actual) throw AssertionError(
                "Objects not equal -  expected: $expected  actual: $actual")
    }

    fun notEquals(expected: Int, actual: Int) {
        if (expected == actual) throw AssertionError()
    }

    fun notEquals(expected: Long, actual: Long) {
        if (expected == actual) throw AssertionError()
    }

    fun notEquals(expected: Short, actual: Short) {
        if (expected == actual) throw AssertionError()
    }

    fun notEquals(expected: Byte, actual: Byte) {
        if (expected == actual) throw AssertionError()
    }

    fun notEquals(expected: Char, actual: Char) {
        if (expected == actual) throw AssertionError()
    }

    fun notEquals(expected: Any, actual: Any?) {
        if (ObjectsCompat.equals(expected, actual)) throw AssertionError()
    }

    fun same(expected: Any, actual: Any) {
        if (expected !== actual) throw AssertionError(
                "Objects not the same -  expected: $expected  actual: $actual")
    }

    fun notSame(expected: Any, actual: Any?) {
        if (expected === actual) throw AssertionError()
    }

    fun lessThan(value1: Int, value2: Int) {
        if (value1 >= value2) throw AssertionError()
    }

    fun lessOrEqual(value1: Int, value2: Int) {
        if (value1 > value2) throw AssertionError()
    }

    fun inRangeInclusive(value1: Int, value2: Int, value3: Int) {
        if (value1 > value2 || value2 > value3) throw AssertionError()
    }

    fun inRangeExclusive(value1: Int, value2: Int, value3: Int) {
        if (value1 >= value2 || value2 >= value3) throw AssertionError()
    }

    fun lessThan(value1: Long, value2: Long) {
        if (value1 >= value2) throw AssertionError()
    }

    fun lessOrEqual(value1: Long, value2: Long) {
        if (value1 > value2) throw AssertionError()
    }

    fun inRangeInclusive(value1: Long, value2: Long, value3: Long) {
        if (value1 > value2 || value2 > value3) throw AssertionError()
    }

    fun inRangeExclusive(value1: Long, value2: Long, value3: Long) {
        if (value1 >= value2 || value2 >= value3) throw AssertionError()
    }

    fun isOneOf(value: Int, vararg set: Int) {
        if (set.none { value == it }) throw AssertionError()
    }

    fun notOneOf(value: Int, vararg set: Int) {
        if (set.any { value == it }) throw AssertionError()
    }

    fun isOneOf(value: Long, vararg set: Long) {
        if (set.none { value == it }) throw AssertionError()
    }

    fun notOneOf(value: Long, vararg set: Long) {
        if (set.any { value == it }) throw AssertionError()
    }

    fun isOneOfObjects(value: Any?, vararg set: Any) {
        if (set.none { value == it }) throw AssertionError()
    }

    fun notOneOfObjects(value: Any?, vararg set: Any) {
        if (set.any { value == it }) throw AssertionError()
    }

    fun isTrue(value: Boolean) {
        isTrue("", value)
    }

    fun isTrue(message: String, value: Boolean) {
        if (!value) throw AssertionError(message)
    }

    fun isFalse(value: Boolean) {
        isFalse("", value)
    }

    fun isFalse(message: String, value: Boolean) {
        if (value) throw AssertionError(message)
    }

    fun fail(function: () -> Any?) {
        try {
            function()
        } catch (e: Throwable) {
            return
        }
        throw AssertionError()
    }

    fun nofail(function: () -> Any?) {
        try {
            function()
        } catch (e: Error) {
            throw e
        } catch (e: RuntimeException) {
            throw e
        }
    }

    fun fail() {
        throw AssertionError()
    }

    fun onThread(thread: Thread) {
        if (!thread.isCurrent()) throw AssertionError()
    }

    fun offThread(thread: Thread) {
        if (thread.isCurrent()) throw AssertionError()
    }

    fun onLooperThread(looper: Looper) {
        onThread(looper.thread)
    }

    fun offLooperThread(looper: Looper) {
        offThread(looper.thread)
    }

    fun onHandlerThread(handler: Handler) {
        onLooperThread(handler.looper)
    }

    fun offHandlerThread(handler: Handler) {
        offLooperThread(handler.looper)
    }

    fun onMainThread() {
        onLooperThread(Looper.getMainLooper())
    }

    fun onWorkerThread() {
        offLooperThread(Looper.getMainLooper())
    }
}
