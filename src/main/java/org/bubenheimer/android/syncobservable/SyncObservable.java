/*
 * Copyright (c) 2015-2016 Uli Bubenheimer.
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

package org.bubenheimer.android.syncobservable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class SyncObservable {
    public interface Observer {
        /**
         * Any variables from the class are valid only during execution of the method.
         * (similar to BroadcastReceiver)
         */
        boolean onEvent(SyncObservable observable);
    }

    @SuppressWarnings("unused")
    public static abstract class SimpleObserver implements Observer {
        protected SimpleObserver() {
        }

        public final boolean onEvent(final SyncObservable observable) {
            onSimpleEvent(observable);
            return false;
        }

        /**
         * Any variables from the class are valid only during the execution of the method.
         * (similar to BroadcastReceiver)
         */
        public abstract void onSimpleEvent(final SyncObservable observable);
    }

    private final List<Observer> observers = new ArrayList<>();

    @SuppressWarnings("unused")
    public final void addObserver(final Observer observer) {
        observers.add(observer);
    }

    @SuppressWarnings("unused")
    public final boolean removeObserver(final Observer observer) {
        return observers.remove(observer);
    }

    @SuppressWarnings("unused")
    public final boolean handleEvent() {
        final int size = observers.size();
        for (int i = 0; i < size; ++i) {
            final boolean handled = observers.get(i).onEvent(this);
            if (handled) {
                return true;
            }
        }
        return false;
    }
}
