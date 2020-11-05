/*
 * Copyright (c) 2015-2020 Uli Bubenheimer. All rights reserved.
 */

package org.bubenheimer.android.app

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

public val SavedStateHandle.delegate: SavedStateDelegate get() = SavedStateDelegate(this)

public class SavedStateDelegate public constructor(
    @PublishedApi internal val state: SavedStateHandle
) {
    public inline operator fun <reified T : Any?> getValue(thisRef: Any?, property: KProperty<*>):
    // Cast to better support nullable vs. non-null types, and to verify general type correctness
            T = state.get<T>(property.name) as T

    public operator fun <T : Any?> setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        state[property.name] = value
    }
}

public val SavedStateHandle.liveDataDelegate: SavedStateLiveDataDelegate
    get() = SavedStateLiveDataDelegate(this)

public class SavedStateLiveDataDelegate public constructor(private val state: SavedStateHandle) {
    public operator fun <T : Any?> getValue(thisRef: Any?, property: KProperty<*>):
            MutableLiveData<T> = state.getLiveData(property.name)
}

public fun <T : Any?> SavedStateHandle.liveDataDelegate(initialValue: T):
        ReadOnlyProperty<Any?, MutableLiveData<T>> =
    SavedStateLiveDataInitialDelegate(this, initialValue)

private class SavedStateLiveDataInitialDelegate<T : Any?> internal constructor(
    private val state: SavedStateHandle,
    private val initialValue: T
) : ReadOnlyProperty<Any?, MutableLiveData<T>> {
    override operator fun getValue(thisRef: Any?, property: KProperty<*>):
            MutableLiveData<T> = state.getLiveData(property.name, initialValue)
}
