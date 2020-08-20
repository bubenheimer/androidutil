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

package org.bubenheimer.android.threading

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.os.Message
import java.util.concurrent.Executor

public class LooperExecutor(looper: Looper, async: Boolean = true) : Executor {
    private val handler = Handler(looper)
    private val async = async && HAS_ASYNC

    @SuppressLint("NewApi")
    public override fun execute(command: Runnable) {
        val msg = Message.obtain(handler, command)
        if (async) {
            msg.isAsynchronous = async
        }
        msg.sendToTarget()
    }
}
