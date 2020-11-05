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

package org.bubenheimer.android.app

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

internal class SavedStateDelegateTest {
    @Test
    internal fun testGetNonNullTypeNonNullValue() {
        val state = SavedStateHandle(mapOf(KEY to VALUE))

        val value: Int by state.delegate

        assertEquals(VALUE, value)
    }

    @Test
    internal fun testGetNonNullTypeNullValue() {
        val state = SavedStateHandle()

        val value: Int by state.delegate

        @Suppress("UNUSED_EXPRESSION")
        assertFailsWith(NullPointerException::class) { value }
    }

    @Test
    internal fun testGetNonNullTypeWrongValueType() {
        val state = SavedStateHandle(mapOf(KEY to VALUE.toLong()))

        val value: Int by state.delegate

        @Suppress("UNUSED_EXPRESSION")
        assertFailsWith(ClassCastException::class) { value }
    }

    @Test
    internal fun testGetNullTypeNonNullValue() {
        val state = SavedStateHandle(mapOf(KEY to VALUE))

        val value: Int? by state.delegate

        assertEquals(VALUE, value)
    }

    @Test
    internal fun testGetNullTypeNullValue() {
        val state = SavedStateHandle()

        val value: Int? by state.delegate

        assertNull(value)
    }

    @Test
    internal fun testGetNullTypeWrongValueType() {
        val state = SavedStateHandle(mapOf(KEY to VALUE.toLong()))

        val value: Int? by state.delegate

        @Suppress("UNUSED_EXPRESSION")
        assertFailsWith(ClassCastException::class) { value }
    }

    @Test
    internal fun testSetNullTypeNullValue() {
        val state = SavedStateHandle()

        @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
        var value: Int? by state.delegate

        @Suppress("UNUSED_VALUE")
        value = null

        assertNull(state[KEY])
    }

    @Test
    internal fun testSetNullTypeNonNullValue() {
        val state = SavedStateHandle()

        @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
        var value: Int? by state.delegate

        @Suppress("UNUSED_VALUE")
        value = VALUE

        @Suppress("RemoveExplicitTypeArguments")
        assertEquals<Int?>(VALUE, state[KEY])
    }

    @Test
    internal fun testGetLiveDataNonNullTypeNullValue() {
        val state = SavedStateHandle()

        val value: MutableLiveData<Int> by state.liveDataDelegate

        assertNull(value.value)
    }

    @Test
    internal fun testGetLiveDataNonNullTypeNonNullValue() {
        val state = SavedStateHandle(mapOf(KEY to VALUE))

        val value: MutableLiveData<Int> by state.liveDataDelegate

        assertEquals(VALUE, value.value)
    }

    @Test
    internal fun testGetLiveDataNullTypeNullValue() {
        val state = SavedStateHandle()

        val value: MutableLiveData<Int?> by state.liveDataDelegate

        assertNull(value.value)
    }

    @Test
    internal fun testGetLiveDataNullTypeNonNullValue() {
        val state = SavedStateHandle(mapOf(KEY to VALUE))

        val value: MutableLiveData<Int?> by state.liveDataDelegate

        assertEquals(VALUE, value.value)
    }

    @Test
    internal fun testGetLiveDataInitialNonNullTypeNonNullValue() {
        val state = SavedStateHandle()

        val value: MutableLiveData<Int> by state.liveDataDelegate(VALUE)

        assertEquals(VALUE, value.value)
    }

    @Test
    internal fun testGetLiveDataInitialNullTypeNullValue() {
        val state = SavedStateHandle()

        val value: MutableLiveData<Int?> by state.liveDataDelegate(null)

        assertNull(value.value)
    }

    @Test
    internal fun testGetLiveDataInitialNullTypeNonNullValue() {
        val state = SavedStateHandle()

        val value: MutableLiveData<Int?> by state.liveDataDelegate(VALUE)

        assertEquals(VALUE, value.value)
    }

    private companion object {
        private const val KEY = "value"
        private const val VALUE = 1
    }
}
