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
package org.bubenheimer.android.ui

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.recyclerview.selection.MutableSelection
import androidx.recyclerview.selection.Selection
import androidx.recyclerview.selection.StorageStrategy

public class IntStorageStrategy : StorageStrategy<Int>(Int::class.java) {
    private companion object {
        private const val SELECTION_ENTRIES = "org.bubenheimer.recyclerview.selection.entries"
        private const val SELECTION_KEY_TYPE = "org.bubenheimer.recyclerview.selection.type"
    }

    public override fun asSelection(state: Bundle): Selection<Int>? {
        val keyType = state.getString(SELECTION_KEY_TYPE)
        if (keyType == null || keyType != Int::class.java.canonicalName) {
            return null
        }

        val stored = state.getIntArray(SELECTION_ENTRIES) ?: return null

        val selection = MutableSelection<Int>()
        stored.forEach { selection.add(it) }
        return selection
    }

    public override fun asBundle(selection: Selection<Int>): Bundle {
        val entries = IntArray(selection.size())
        selection.forEachIndexed { index: Int, key: Int -> entries[index] = key }

        return bundleOf(
                SELECTION_KEY_TYPE to Int::class.java.canonicalName,
                SELECTION_ENTRIES to entries
        )
    }
}
