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
 * at the cost of one additional object per [androidx.lifecycle.LifecycleObserver].
 */
public class ProxyLifecycleObserver(private val delegate: Delegate) : DefaultLifecycleObserver {
    public interface Delegate {
        public fun onCreate(owner: LifecycleOwner) {}
        public fun onStart(owner: LifecycleOwner) {}
        public fun onResume(owner: LifecycleOwner) {}
        public fun onPause(owner: LifecycleOwner) {}
        public fun onStop(owner: LifecycleOwner) {}
        public fun onDestroy(owner: LifecycleOwner) {}
    }

    public override fun onCreate(owner: LifecycleOwner): Unit = delegate.onCreate(owner)
    public override fun onStart(owner: LifecycleOwner): Unit = delegate.onStart(owner)
    public override fun onResume(owner: LifecycleOwner): Unit = delegate.onResume(owner)
    public override fun onPause(owner: LifecycleOwner): Unit = delegate.onPause(owner)
    public override fun onStop(owner: LifecycleOwner): Unit = delegate.onStop(owner)
    public override fun onDestroy(owner: LifecycleOwner): Unit = delegate.onDestroy(owner)
}
