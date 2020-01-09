package iv.nakonechnyi.preferancepresentation.preferences

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.preference.PreferenceDialogFragmentCompat
import iv.nakonechnyi.preferancepresentation.R

class AutoCompleteDialogFragmentCompat : PreferenceDialogFragmentCompat() {

    companion object {

        private val SAVE_STATE_TEXT = "AutoCompletePreference.text"
        private val SAVE_STATE_ENTRIES = "AutoCompletePreference.entries"
        private val EMPTY_VALUE = ""

        fun newInstance(key: String) =
            AutoCompleteDialogFragmentCompat().apply {
                arguments = Bundle(1).apply {
                    putString(ARG_KEY, key)
                }
            }
    }

    private lateinit var text: String

    private lateinit var entries: Array<CharSequence>

    private lateinit var editText: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val pref = preference as AutoCompletePreference
            text = pref.text
            entries = pref.entries
        } else {
            text = savedInstanceState.getString(SAVE_STATE_TEXT, EMPTY_VALUE)
            entries = savedInstanceState.getCharSequenceArray(SAVE_STATE_ENTRIES) ?: arrayOf()

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVE_STATE_TEXT, text)
        outState.putCharSequenceArray(SAVE_STATE_ENTRIES, entries)
    }

    override fun onCreateDialogView(context: Context?): View =
        LayoutInflater.from(context).inflate(R.layout.preference_dialog_autocompletetext, null)

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)
        val textAdapter = ArrayAdapter<CharSequence>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            entries
        )
        editText = (view.findViewById(R.id.input) as AutoCompleteTextView).apply {
            requestFocus()
            setAdapter(textAdapter)
            setText(this@AutoCompleteDialogFragmentCompat.text)
        }
        val pref = preference as AutoCompletePreference
        pref.bindAutocompleteListener?.bindAutoComplete(editText)
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult){
            val value = editText.text.toString()
            val pref = preference as AutoCompletePreference
            if (pref.callChangeListener(value)) {
                pref.text = value
            }
        }
    }
}