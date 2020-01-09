package iv.nakonechnyi.preferancepresentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.*
import iv.nakonechnyi.preferancepresentation.preferences.AutoCompleteDialogFragmentCompat
import iv.nakonechnyi.preferancepresentation.preferences.AutoCompletePreference
import iv.nakonechnyi.preferancepresentation.preferences.DatePreference
import iv.nakonechnyi.preferancepresentation.preferences.DatePreferenceDialogFragmentCompat

class SettingsActivity : AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPreferenceDisplayDialog(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean = when (pref) {
        is DatePreference -> DatePreferenceDialogFragmentCompat.newInstance(pref.key)
        is AutoCompletePreference -> AutoCompleteDialogFragmentCompat.newInstance(pref.key)
        else -> throw IllegalArgumentException()
    }
        .apply { setTargetFragment(caller, 0) }
        .show(supportFragmentManager, "iv.nakonechnyi.preferencepresentation.DatePreferenceDialog")
        .run { true }


    class SettingsFragment : PreferenceFragmentCompat(),
        DialogPreference.TargetFragment {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}