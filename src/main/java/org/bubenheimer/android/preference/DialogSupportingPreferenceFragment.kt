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
package org.bubenheimer.android.preference

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

abstract class DialogSupportingPreferenceFragment : PreferenceFragmentCompat() {
    private companion object {
        private const val DIALOG_FRAGMENT_TAG =
                "org.bubenheimer.android.preference.DialogSupportingPreferenceFragment.DIALOG"
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        (activity as? OnPreferenceDisplayDialogCallback)
                ?.takeIf { it.onPreferenceDisplayDialog(this, preference) }?.let { return }

        if (preference is DialogSupporter) {
            val fragmentManager = parentFragmentManager
            if (fragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) == null) {
                //Dialog is not showing yet
                val dialogFragment = (preference as DialogSupporter).newDialog()
                dialogFragment.setTargetFragment(this, 0)
                dialogFragment.show(fragmentManager, DIALOG_FRAGMENT_TAG)
            }
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }
}
