package iv.nakonechnyi.preferancepresentation.preferences

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.DatePicker
import androidx.preference.PreferenceDialogFragmentCompat
import java.text.SimpleDateFormat
import java.util.*

class DatePreferenceDialogFragmentCompat :
    PreferenceDialogFragmentCompat(), DatePickerDialog.OnDateSetListener {

    private var date: String? = null

    private lateinit var dialog: DatePickerDialog

    companion object {

        private val DATE_STRING_KEY = "DatePreferenceDialog.DATE"
        private val DATE_FORMAT_STRING = "yyyy-MM-dd"

        fun newInstance(key: String) =
            DatePreferenceDialogFragmentCompat().apply {
                arguments = Bundle(1).apply {
                    putString(ARG_KEY, key)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        date = if (savedInstanceState == null) {
            val pref = preference as DatePreference
            pref.date
        } else {
            savedInstanceState.getString(DATE_STRING_KEY)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(DATE_STRING_KEY, date)
    }

    override fun onDateSet(picker: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance().apply {
            set(year, month, day)
        }
        date = SimpleDateFormat(DATE_FORMAT_STRING, Locale.getDefault()).format(calendar.time)
        onClick(dialog, DialogInterface.BUTTON_POSITIVE)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val savedTimeInMillis = date?.let {
            SimpleDateFormat(DATE_FORMAT_STRING, Locale.getDefault()).parse(it)?.time
        }

        val calendar = Calendar.getInstance().apply {
            if (savedTimeInMillis != null) {
                timeInMillis = savedTimeInMillis
            }
        }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        dialog = DatePickerDialog(requireContext(), this, year, month, day)
        return dialog
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val pref = preference as DatePreference
            if (pref.callChangeListener(date)) {
                pref.date = date
            }
        }
    }
}