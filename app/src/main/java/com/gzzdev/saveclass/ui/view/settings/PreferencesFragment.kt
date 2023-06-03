package com.gzzdev.saveclass.ui.view.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.gzzdev.saveclass.R

class PreferencesFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}