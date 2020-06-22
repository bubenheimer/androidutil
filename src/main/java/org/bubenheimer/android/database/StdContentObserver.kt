/*
 * Copyright (c) 2015-2019 Uli Bubenheimer
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
package org.bubenheimer.android.database

import android.annotation.TargetApi
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Handler

abstract class StdContentObserver(handler: Handler?) : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean) = throw UnsupportedOperationException()

    @TargetApi(Build.VERSION_CODES.R)
    override fun onChange(selfChange: Boolean, uri: Uri?, flags: Int) = onChange(selfChange, uri)

    @TargetApi(Build.VERSION_CODES.R)
    override fun onChange(selfChange: Boolean, uris: Collection<Uri>, flags: Int) =
            uris.forEach { onChange(selfChange, it) }
}
