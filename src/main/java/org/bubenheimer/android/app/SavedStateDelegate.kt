/*
 * Copyright (c) 2015-2020 Uli Bubenheimer. All rights reserved.
 */

package org.bubenheimer.android.app

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

public val SavedStateHandle.delegate: SavedStateDelegate get() = SavedStateDelegate(this)

public class SavedStateDelegate public constructor(private val state: SavedStateHandle) {
    @Suppress("UNCHECKED_CAST")
    public operator fun <T : Any?> getValue(thisRef: Any?, property: KProperty<*>): T =
        // Cast to better support nullable vs. non-null types
        state.get<T>(property.name) as T

    public operator fun <T : Any?> setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        state[property.name] = value
    }
}

public val SavedStateHandle.stateFlowDelegate: SavedStateFlowDelegate
    get() = SavedStateFlowDelegate(this)

public class SavedStateFlowDelegate public constructor(private val state: SavedStateHandle) {
    public operator fun <T : Any?> getValue(thisRef: Any?, property: KProperty<*>):
            MutableStateFlow<T> {
        @Suppress("UNCHECKED_CAST")
        val flow = MutableStateFlow(state.get<Bundle>(property.name)?.get(INNER_KEY) as T)

        state.setSavedStateProvider(property.name) { bundleOf(INNER_KEY to flow.value) }
        return flow
    }

    private companion object {
        private const val INNER_KEY: String = "key"
    }
}

public fun <T : Any?> SavedStateHandle.stateFlowDelegate(initialValue: T):
        ReadOnlyProperty<Any?, MutableStateFlow<T>> =
    SavedStateStateFlowInitialDelegate(this, initialValue)

private class SavedStateStateFlowInitialDelegate<T : Any?> constructor(
    private val state: SavedStateHandle,
    private val initialValue: T
) : ReadOnlyProperty<Any?, MutableStateFlow<T>> {
    override operator fun getValue(thisRef: Any?, property: KProperty<*>): MutableStateFlow<T> {
        val bundle: Bundle? = state.get(property.name)

        @Suppress("UNCHECKED_CAST")
        val flow = MutableStateFlow(if (bundle == null) initialValue else bundle[INNER_KEY] as T)

        state.setSavedStateProvider(property.name) { bundleOf(INNER_KEY to flow.value) }
        return flow
    }

    private companion object {
        private const val INNER_KEY: String = "key"
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

private class SavedStateLiveDataInitialDelegate<T : Any?> constructor(
    private val state: SavedStateHandle,
    private val initialValue: T
) : ReadOnlyProperty<Any?, MutableLiveData<T>> {
    override operator fun getValue(thisRef: Any?, property: KProperty<*>): MutableLiveData<T> =
        state.getLiveData(property.name, initialValue)
}
