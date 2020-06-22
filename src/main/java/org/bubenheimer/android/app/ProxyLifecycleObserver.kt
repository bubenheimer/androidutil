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
package org.bubenheimer.android.app

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Proxies [DefaultLifecycleObserver] with a delegate pattern to enable obfuscation,
 * at the cost of one additional object per [LifecycleObserver].
 */
class ProxyLifecycleObserver(private val delegate: Delegate) : DefaultLifecycleObserver {
    interface Delegate {
        fun onCreate(owner: LifecycleOwner) {}
        fun onStart(owner: LifecycleOwner) {}
        fun onResume(owner: LifecycleOwner) {}
        fun onPause(owner: LifecycleOwner) {}
        fun onStop(owner: LifecycleOwner) {}
        fun onDestroy(owner: LifecycleOwner) {}
    }

    override fun onCreate(owner: LifecycleOwner) = delegate.onCreate(owner)
    override fun onStart(owner: LifecycleOwner) = delegate.onStart(owner)
    override fun onResume(owner: LifecycleOwner) = delegate.onResume(owner)
    override fun onPause(owner: LifecycleOwner) = delegate.onPause(owner)
    override fun onStop(owner: LifecycleOwner) = delegate.onStop(owner)
    override fun onDestroy(owner: LifecycleOwner) = delegate.onDestroy(owner)
}
