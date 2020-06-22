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

package org.bubenheimer.android.app;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * Proxies {@link DefaultLifecycleObserver} with a delegate pattern to enable obfuscation,
 * at the cost of one additional object per {@link LifecycleObserver}.
 */
public final class ProxyLifecycleObserver implements DefaultLifecycleObserver {
    @SuppressWarnings("unused")
    public interface Delegate {
        default void onCreate(
                @NonNull LifecycleOwner owner) {
        }
        default void onStart(
                @NonNull LifecycleOwner owner) {
        }
        default void onResume(
                @NonNull LifecycleOwner owner) {
        }
        default void onPause(
                @NonNull LifecycleOwner owner) {
        }
        default void onStop(
                @NonNull LifecycleOwner owner) {
        }
        default void onDestroy(
                @NonNull LifecycleOwner owner) {
        }
    }

    private final @NonNull Delegate delegate;

    public ProxyLifecycleObserver(
            final @NonNull Delegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onCreate(
            final @NonNull LifecycleOwner owner) {
        delegate.onCreate(owner);
    }

    @Override
    public void onStart(
            final @NonNull LifecycleOwner owner) {
        delegate.onStart(owner);
    }

    @Override
    public void onResume(
            final @NonNull LifecycleOwner owner) {
        delegate.onResume(owner);
    }

    @Override
    public void onPause(
            final @NonNull LifecycleOwner owner) {
        delegate.onPause(owner);
    }

    @Override
    public void onStop(
            final @NonNull LifecycleOwner owner) {
        delegate.onStop(owner);
    }

    @Override
    public void onDestroy(
            final @NonNull LifecycleOwner owner) {
        delegate.onDestroy(owner);
    }
}
