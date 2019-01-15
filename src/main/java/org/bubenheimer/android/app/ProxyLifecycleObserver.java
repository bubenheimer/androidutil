/*
 * Copyright (c) 2015-2019 Uli Bubenheimer. All rights reserved.
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

    private final Delegate delegate;

    public ProxyLifecycleObserver(
            final Delegate delegate) {
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
