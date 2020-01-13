package iv.nakonechnyi.preferancepresentation

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*
import iv.nakonechnyi.preferancepresentation.preferences.*

class SettingsActivity :
    AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)


        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == "picker")
        supportActionBar?.setBackgroundDrawable(ColorDrawable(sharedPreferences.getInt(key, Color.RED)))
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this)
    }

    class SettingsFragment :
        PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val pref = preferenceManager.findPreference<AutoCompletePreference>("auto")
            pref?.bindAutocompleteListener = object : AutoCompletePreference.AutocompleteBinder {
                override fun bindAutoComplete(textView: AutoCompleteTextView) {
                    with(textView) {
                        //                        inputType = InputType.TYPE_CLASS_NUMBER
                    }
                }
            }
        }


        override fun onDisplayPreferenceDialog(
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
        } ?: Unit


    }

}