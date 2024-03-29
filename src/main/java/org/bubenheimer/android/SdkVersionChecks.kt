/*
 * Copyright (c) 2015-2023 Uli Bubenheimer
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

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

public fun sdkBelow(version: Int): Boolean = Build.VERSION.SDK_INT < version

@ChecksSdkIntAtLeast(parameter = 0)
public fun sdkAtLeast(version: Int): Boolean = version <= Build.VERSION.SDK_INT

public inline fun belowSdk(version: Int, block: () -> Unit) {
    if (sdkBelow(version)) block()
}

@ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
public inline fun fromSdk(version: Int, block: () -> Unit) {
    if (sdkAtLeast(version)) block()
}

public inline fun <T, S : T> S.belowSdk(version: Int, block: S.() -> T): T =
    if (sdkBelow(version)) block() else this

@ChecksSdkIntAtLeast(parameter = 1, lambda = 2)
public inline fun <T, S : T> S.fromSdk(version: Int, block: S.() -> T): T =
    if (sdkAtLeast(version)) block() else this
