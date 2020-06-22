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

package org.bubenheimer.android.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.MutableSelection;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.StorageStrategy;

public final class IntStorageStrategy extends StorageStrategy<Integer> {
    private static final String SELECTION_ENTRIES =
            "org.bubenheimer.recyclerview.selection.entries";
    private static final String SELECTION_KEY_TYPE =
            "org.bubenheimer.recyclerview.selection.type";

    public IntStorageStrategy() {
        super(Integer.class);
    }

    @Override
    public @Nullable Selection<Integer> asSelection(
            final @NonNull Bundle state) {
        final String keyType = state.getString(SELECTION_KEY_TYPE, null);
        if (keyType == null || !keyType.equals(Integer.class.getCanonicalName())) {
            return null;
        }

        final @Nullable int[] stored = state.getIntArray(SELECTION_ENTRIES);
        if (stored == null) {
            return null;
        }

        final MutableSelection<Integer> selection = new MutableSelection<>();
        for (final int key : stored) {
            selection.add(key);
        }
        return selection;
    }

    @Override
    public @NonNull Bundle asBundle(
            final @NonNull Selection<Integer> selection) {
        final Bundle bundle = new Bundle();
        bundle.putString(SELECTION_KEY_TYPE, Integer.class.getCanonicalName());

        final int[] value = new int[selection.size()];
        int i = 0;
        for (final Integer key : selection) {
            value[i++] = key;
        }
        bundle.putIntArray(SELECTION_ENTRIES, value);

        return bundle;
    }
}
