/*
 * Copyright (c) 2015-2021 Uli Bubenheimer
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

import android.os.Parcel
import android.os.Parcelable
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * For optimal performance: call this reflective method once per class and store the result
 */
@Suppress("UNCHECKED_CAST")
public inline val <T : Parcelable> Class<T>.creator: Parcelable.Creator<T>
    get() = getDeclaredField("CREATOR").get(null) as Parcelable.Creator<T>

public inline fun <T> withParcel(block: (Parcel) -> T): T {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }

    val parcel = Parcel.obtain()
    try {
        return block(parcel)
    } finally {
        parcel.recycle()
    }
}
