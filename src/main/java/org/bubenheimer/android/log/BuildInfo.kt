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

package org.bubenheimer.android.log

import android.os.Build.BOARD
import android.os.Build.BOOTLOADER
import android.os.Build.BRAND
import android.os.Build.DEVICE
import android.os.Build.DISPLAY
import android.os.Build.FINGERPRINT
import android.os.Build.HARDWARE
import android.os.Build.HOST
import android.os.Build.ID
import android.os.Build.MANUFACTURER
import android.os.Build.MODEL
import android.os.Build.ODM_SKU
import android.os.Build.PRODUCT
import android.os.Build.SKU
import android.os.Build.SOC_MANUFACTURER
import android.os.Build.SOC_MODEL
import android.os.Build.TAGS
import android.os.Build.TIME
import android.os.Build.TYPE
import android.os.Build.USER
import android.os.Build.VERSION.BASE_OS
import android.os.Build.VERSION.CODENAME
import android.os.Build.VERSION.INCREMENTAL
import android.os.Build.VERSION.MEDIA_PERFORMANCE_CLASS
import android.os.Build.VERSION.PREVIEW_SDK_INT
import android.os.Build.VERSION.RELEASE
import android.os.Build.VERSION.RELEASE_OR_CODENAME
import android.os.Build.VERSION.RELEASE_OR_PREVIEW_DISPLAY
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION.SECURITY_PATCH
import android.os.Build.VERSION_CODES.R
import android.os.Build.VERSION_CODES.S
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Build.getRadioVersion
import androidx.annotation.ChecksSdkIntAtLeast
import org.bubenheimer.android.sdkAtLeast

public fun logBuild(priority: Log.Priority, tag: String): Unit = Log.println(
    priority,
    tag,
    """
        BOARD            $BOARD
        BOOTLOADER       $BOOTLOADER
        BRAND            $BRAND
        DEVICE           $DEVICE
        DISPLAY          $DISPLAY
        FINGERPRINT      $FINGERPRINT
        HARDWARE         $HARDWARE
        HOST             $HOST
        ID               $ID
        MANUFACTURER     $MANUFACTURER
        MODEL            $MODEL
        ODM_SKU          ${fromSdk(S) { ODM_SKU }}
        PRODUCT          $PRODUCT
        RADIO            ${getRadioVersion()}
        SKU              ${fromSdk(S) { SKU }}
        SOC_MANUFACTURER ${fromSdk(S) { SOC_MANUFACTURER }}
        SOC_MODEL        ${fromSdk(S) { SOC_MODEL }}
        TAGS             $TAGS
        TIME             $TIME
        TYPE             $TYPE
        USER             $USER

        BASE_OS          $BASE_OS
        CODENAME         $CODENAME
        INCREMENTAL      $INCREMENTAL
        MEDIA_PERF_CLASS ${fromSdk(S) { MEDIA_PERFORMANCE_CLASS }}
        PREVIEW_SDK_INT  $PREVIEW_SDK_INT
        RELEASE          $RELEASE
        RELEASE|CODENAME ${fromSdk(R) { RELEASE_OR_CODENAME }}
        RELEASE|PREVIEW  ${fromSdk(TIRAMISU) { RELEASE_OR_PREVIEW_DISPLAY }}
        SDK              $SDK_INT
        SECURITY_PATCH   $SECURITY_PATCH
    """.trimIndent()
)

@ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
private inline fun fromSdk(version: Int, block: () -> Any): Any =
    if (sdkAtLeast(version)) block() else ""
