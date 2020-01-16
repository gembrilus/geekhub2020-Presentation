package iv.nakonechnyi.preferancepresentation

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*
import iv.nakonechnyi.preferancepresentation.preferences.AutoCompleteDialogFragmentCompat
import iv.nakonechnyi.preferancepresentation.preferences.AutoCompletePreference
import iv.nakonechnyi.preferancepresentation.preferences.DatePreference
import iv.nakonechnyi.preferancepresentation.preferences.DatePreferenceDialogFragmentCompat

class SettingsActivity :
    AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener,
    PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)


        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == "picker")
            supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    sharedPreferences.getInt(
                        key,
                        Color.RED
                    )
                )
            )
    }

    //It needs for a displaying of a preference dialog

    override fun onPreferenceDisplayDialog(
        caller: PreferenceFragmentCompat,
        pref: Preference?
    ): Boolean =
        when (pref) {
            is DatePreference -> DatePreferenceDialogFragmentCompat.newInstance(pref.key)
            is AutoCompletePreference -> AutoCompleteDialogFragmentCompat.newInstance(pref.key)
            is EditTextPreference -> EditTextPreferenceDialogFragmentCompat.newInstance(pref.key)
            else -> throw IllegalArgumentException()
        }
            .apply { setTargetFragment(caller, 0) }
            .show(
                supportFragmentManager,
                "iv.nakonechnyi.preferencepresentation.DatePreferenceDialog"
            )
            .run { true }


    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }


    class SettingsFragment :
        PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val pref = preferenceManager.findPreference<AutoCompletePreference>("auto")
            pref?.bindAutocompleteListener = object : AutoCompletePreference.AutocompleteBinder {
                override fun bindAutoComplete(textView: AutoCompleteTextView) {

                    /*with(textView) {
                          inputType = InputType.TYPE_CLASS_NUMBER
                    }*/
                }
            }
        }


/*        override fun onDisplayPreferenceDialog(
            pref: Preference
        ) = fragmentManager?.let {
            when (pref) {
                is DatePreference -> DatePreferenceDialogFragmentCompat.newInstance(pref.key)
                is AutoCompletePreference -> AutoCompleteDialogFragmentCompat.newInstance(pref.key)
                is EditTextPreference -> EditTextPreferenceDialogFragmentCompat.newInstance(pref.key)
                else -> throw IllegalArgumentException()
            }
                .apply { setTargetFragment(this@SettingsFragment, 0) }
                .show(it, "iv.nakonechnyi.preferencepresentation.DatePreferenceDialog")
        } ?: super.onDisplayPreferenceDialog(pref)*/


    }
}