/*
 * Copyright (c) 2015-2020 Uli Bubenheimer
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

import android.os.Handler
import android.os.Looper
import org.bubenheimer.util.isCurrent

//TODO refactor the first 2 methods

private fun examOnThread(thread: Thread, msg: String = "") {
    if (!thread.isCurrent()) throw AssertionError(
        "Not on expected thread \"$thread\"  msg: \"$msg\""
    )
}

private fun examOffThread(thread: Thread, msg: String = "") {
    if (thread.isCurrent()) throw AssertionError(
        "Not off thread \"$thread\"  msg: \"$msg\""
    )
}

public fun examOnLooperThread(looper: Looper, msg: String = "") {
    examOnThread(looper.thread, "Not on Looper thread; msg: \"$msg\" ")
}

public fun examOffLooperThread(looper: Looper, msg: String = "") {
    examOffThread(looper.thread, "Not off Looper thread; msg: \"$msg\" ")
}

public fun examOnHandlerThread(handler: Handler, msg: String = "") {
    examOnLooperThread(handler.looper, "Not on thread for Handler $handler; msg: \"$msg\" ")
}

public fun examOffHandlerThread(handler: Handler, msg: String = "") {
    examOffLooperThread(handler.looper, "Not off thread for Handler $handler; msg: \"$msg\" ")
}

public fun examOnMainThread(msg: String = "") {
    examOnLooperThread(Looper.getMainLooper(), "Not on main thread; msg: \"$msg\" ")
}

public fun examOnWorkerThread(msg: String = "") {
    examOffLooperThread(Looper.getMainLooper(), "Not on worker thread; msg: \"$msg\" ")
}
