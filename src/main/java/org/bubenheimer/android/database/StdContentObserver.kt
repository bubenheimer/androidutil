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

import android.database.ContentObserver
import android.net.Uri
import android.os.Build.VERSION_CODES.R
import android.os.Handler
import androidx.annotation.RequiresApi

public abstract class StdContentObserver(handler: Handler?) : ContentObserver(handler) {
    public override fun onChange(selfChange: Boolean): Unit = throw UnsupportedOperationException()

    @RequiresApi(R)
    public override fun onChange(selfChange: Boolean, uri: Uri?, flags: Int): Unit =
        onChange(selfChange, uri)

    @RequiresApi(R)
    public override fun onChange(selfChange: Boolean, uris: Collection<Uri>, flags: Int): Unit =
        uris.forEach { onChange(selfChange, it) }
}
