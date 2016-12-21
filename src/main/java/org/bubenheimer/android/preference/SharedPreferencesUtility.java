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

package org.bubenheimer.android.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v4.util.Pair;
import android.support.v4.util.SimpleArrayMap;

import org.bubenheimer.android.log.Log;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class SharedPreferencesUtility {
    private static final String TAG = SharedPreferencesUtility.class.getSimpleName();

    private static final SimpleArrayMap<SharedPreferences, SimpleArrayMap<String, Pair<Integer,
                    ? extends List<OnSharedPreferenceChangeListener>>>> masterMap =
            new SimpleArrayMap<>();

    private static final SharedPreferences.OnSharedPreferenceChangeListener masterListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @UiThread
                @Override
                public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences,
                                                      final String key) {
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
                    final int size = list.size();
                    for (int i = 0; i < size; ++i) {
                        list.get(i).onSharedPreferenceChanged(sharedPreferences, pair.first, key);
                    }
                }
            };

    public interface OnSharedPreferenceChangeListener {
        @UiThread
        void onSharedPreferenceChanged(@NonNull final SharedPreferences sharedPreferences,
                                       @StringRes int id, @NonNull String key);
    }

    @UiThread
    public static void registerOnSharedPreferenceChangeListener(
            @NonNull final Context context, @NonNull final SharedPreferences prefs,
            @NonNull final OnSharedPreferenceChangeListener listener,
            @StringRes final int... resIds) {
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
                pair = Pair.create(resId, new ArrayList<OnSharedPreferenceChangeListener>());
                prefsEntry.put(key, pair);
            }
            pair.second.add(listener);
        }
    }

    @UiThread
    public static void unregisterOnSharedPreferenceChangeListener(
            @NonNull final Context context, @NonNull final SharedPreferences prefs,
            @NonNull final OnSharedPreferenceChangeListener listener,
            @StringRes final int... resIds) {
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

    private SharedPreferencesUtility() {
        throw new UnsupportedOperationException();
    }
}
