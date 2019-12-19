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

package org.bubenheimer.android.preference;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;
import androidx.core.util.Pair;
import androidx.collection.SimpleArrayMap;

import org.bubenheimer.android.internal.CheckInternal;
import org.bubenheimer.android.log.Log;
import org.bubenheimer.util.Uninstantiable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused"})
public final class SharedPreferencesUtility extends Uninstantiable {
    private static final String TAG = SharedPreferencesUtility.class.getSimpleName();

    private static final SimpleArrayMap<SharedPreferences, SimpleArrayMap<String, Pair<Integer,
                    ? extends List<OnSharedPreferenceChangeListener>>>> masterMap =
            new SimpleArrayMap<>();

    @SuppressWarnings("UnnecessaryLambda")
    private static final SharedPreferences.OnSharedPreferenceChangeListener masterListener =
            (sharedPreferences, key) -> {
                final SimpleArrayMap<String, Pair<Integer,
                        ? extends List<OnSharedPreferenceChangeListener>>> prefsEntry =
                        masterMap.get(sharedPreferences);
                if (prefsEntry == null) {
                    Log.v(TAG, "Shared preferences not registered: ", sharedPreferences);
                    return;
                }

                final Pair<Integer, ? extends List<OnSharedPreferenceChangeListener>> pair =
                        prefsEntry.get(key);
                if (pair == null) {
                    Log.v(TAG, "Shared preference not registered: ",
                            key, " - ", sharedPreferences);
                    return;
                }

                final List<OnSharedPreferenceChangeListener> list = pair.second;
                CheckInternal.notNull(list);
                final int size = list.size();
                for (int i = 0; i < size; ++i) {
                    final @StringRes Integer id = pair.first;
                    CheckInternal.notNull(id);
                    list.get(i).onSharedPreferenceChanged(sharedPreferences, id, key);
                }
            };

    public interface OnSharedPreferenceChangeListener {
        @UiThread
        void onSharedPreferenceChanged(
                @NonNull SharedPreferences sharedPreferences,
                @StringRes int id,
                @NonNull String key
        );
    }

    @UiThread
    public static void registerOnSharedPreferenceChangeListener(
            final @NonNull Context context,
            final @NonNull SharedPreferences prefs,
            final @NonNull OnSharedPreferenceChangeListener listener,
            final @StringRes int... resIds
    ) {
        SimpleArrayMap<String, Pair<Integer, ? extends List<OnSharedPreferenceChangeListener>>>
                prefsEntry = masterMap.get(prefs);
        if (prefsEntry == null) {
            prefsEntry = new SimpleArrayMap<>();
            masterMap.put(prefs, prefsEntry);
            prefs.registerOnSharedPreferenceChangeListener(masterListener);
        }
        final int cnt = resIds.length;
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < cnt; ++i) {
            final int resId = resIds[i];
            final String key = context.getString(resId);
            Pair<Integer, ? extends List<OnSharedPreferenceChangeListener>> pair =
                    prefsEntry.get(key);
            if (pair == null) {
                pair = Pair.create(resId, new ArrayList<>());
                prefsEntry.put(key, pair);
            }
            final List<OnSharedPreferenceChangeListener> listeners = pair.second;
            CheckInternal.notNull(listeners);
            listeners.add(listener);
        }
    }

    @UiThread
    public static void unregisterOnSharedPreferenceChangeListener(
            final @NonNull Context context,
            final @NonNull SharedPreferences prefs,
            final @NonNull OnSharedPreferenceChangeListener listener,
            final @StringRes int... resIds
    ) {
        final SimpleArrayMap<String, Pair<Integer,
                ? extends List<OnSharedPreferenceChangeListener>>>
                prefsEntry = masterMap.get(prefs);
        if (prefsEntry == null) {
            Log.w(TAG, "Shared preferences not registered: ", prefs);
            return;
        }
        final int cnt = resIds.length;
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < cnt; ++i) {
            final int resId = resIds[i];
            final String key = context.getString(resId);
            final Pair<Integer, ? extends List<OnSharedPreferenceChangeListener>> pair =
                    prefsEntry.get(key);
            if (pair == null) {
                Log.w(TAG, "Shared preference not registered: ", key, " - ", prefs);
                return;
            }
            final List<OnSharedPreferenceChangeListener> list = pair.second;
            CheckInternal.notNull(list);
            if (!list.remove(listener)) {
                Log.w(TAG, "Listener not registered: ", key, " - ", prefs);
                return;
            }
            if (list.isEmpty()) {
                prefsEntry.remove(key);
            }
        }
        if (prefsEntry.isEmpty()) {
            masterMap.remove(prefs);
            prefs.unregisterOnSharedPreferenceChangeListener(masterListener);
        }
    }
}
