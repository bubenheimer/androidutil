/*
 * Copyright (c) 2015-2017 Uli Bubenheimer.
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

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.support.v7.preference.PreferenceFragmentCompat;

public abstract class DialogSupportingPreferenceFragment extends PreferenceFragmentCompat {
    private static final String DIALOG_FRAGMENT_TAG =
            "org.bubenheimer.android.preference.DialogSupportingPreferenceFragment.DIALOG";

    @Override
    public void onDisplayPreferenceDialog(final Preference preference) {
        final FragmentActivity activity = getActivity();
        if (activity instanceof PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback &&
                ((PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback) activity)
                        .onPreferenceDisplayDialog(this, preference)) {
            return;
        }

        if (preference instanceof DialogSupporter) {
            final FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) == null) {
                //Dialog is not showing yet
                final PreferenceDialogFragmentCompat dialogFragment =
                        ((DialogSupporter) preference).newDialog();
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(fragmentManager, DIALOG_FRAGMENT_TAG);
            }
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
