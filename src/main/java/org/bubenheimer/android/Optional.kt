/*
 * Copyright (c) 2015-2020 Uli Bubenheimer. All rights reserved.
 */

package org.bubenheimer.android

import androidx.annotation.CheckResult

sealed class Optional<out T> {
    class Some<T>(val value: T) : Optional<T>()
    object Empty : Optional<Nothing>()

    companion object {
        @CheckResult
        fun <T> ofNullable(value: T?): Optional<T> = value?.let {
            Some(value)
        } ?: Empty

        @CheckResult
        fun <T> of(value: T): Optional<T> =
            Some(value)
    }
}

@get:CheckResult
val <T> Optional<T>.isPresent: Boolean get() = this is Optional.Some

@CheckResult
fun <T> Optional<T>.orElse(other: T?): T? = when (this) {
    is Optional.Some -> value
    is Optional.Empty -> other
}

@CheckResult
fun <T> Optional<T>.get(): T = when (this) {
    is Optional.Some -> value
    is Optional.Empty -> throw NoSuchElementException()
}
