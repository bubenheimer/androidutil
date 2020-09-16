/*
 * Copyright (c) 2015-2020 Uli Bubenheimer. All rights reserved.
 */

package org.bubenheimer.android

import androidx.annotation.CheckResult

public sealed class Optional<out T : Any> {
    public class Some<out T : Any>(public val value: T) : Optional<T>()
    public object Empty : Optional<Nothing>()

    public companion object {
        @CheckResult
        public fun <T : Any> ofNullable(value: T?): Optional<T> = value?.let {
            Some(value)
        } ?: Empty

        @CheckResult
        public fun <T : Any> of(value: T): Optional<T> =
            Some(value)
    }
}

@get:CheckResult
public val <T : Any> Optional<T>.isPresent: Boolean
    get() = this is Optional.Some

@CheckResult
public fun <T : Any> Optional<T>.orElse(other: T?): T? = when (this) {
    is Optional.Some -> value
    is Optional.Empty -> other
}

@CheckResult
public fun <T : Any> Optional<T>.get(): T = when (this) {
    is Optional.Some -> value
    is Optional.Empty -> throw NoSuchElementException()
}
