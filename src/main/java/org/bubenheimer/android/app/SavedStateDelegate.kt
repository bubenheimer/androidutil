/*
 * Copyright (c) 2015-2020 Uli Bubenheimer. All rights reserved.
 */

package org.bubenheimer.android.app

import androidx.lifecycle.SavedStateHandle
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

//TODO Alternative implementation that cannot use extension property and looks overly expensive,
// but unable to verify as Kotlin bytecode generation fails; leaving around for a bit in case
// current implementation reveals issues
//
//public inline fun <reified T : Any?> SavedStateHandle.delegate(): ReadWriteProperty<Any?, T> =
//    object : ReadWriteProperty<Any?, T> {
//        override operator fun getValue(thisRef: Any?, property: KProperty<*>):
//                T = this@delegate.get<T>(property.name) as T
//
//        override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
//            this@delegate[property.name] = value
//        }
//    }
