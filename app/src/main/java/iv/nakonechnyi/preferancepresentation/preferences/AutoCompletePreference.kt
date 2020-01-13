package iv.nakonechnyi.preferancepresentation.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.res.TypedArray
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.AutoCompleteTextView
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.DialogPreference
import iv.nakonechnyi.preferancepresentation.R

class AutoCompletePreference(
    context: Context,
    attrs: AttributeSet? = null
) : DialogPreference(context, attrs) {

    companion object {

        private const val EMPTY_VALUE = ""

    }

    interface AutocompleteBinder {
        fun bindAutoComplete(textView: AutoCompleteTextView)
    }

    init {
        dialogLayoutResource = R.layout.preference_dialog_autocompletetext
    }

    var text: String = EMPTY_VALUE
        set(value) {
            if (!TextUtils.equals(value, text)) {
                field = value
                persistString(value)
                notifyChanged()
            }
        }

    val entries: Array<CharSequence>

    var bindAutocompleteListener: AutocompleteBinder? = null

    private var mSummary: CharSequence? = null

    init {

        val a = context.obtainStyledAttributes(
            attrs, R.styleable.AutoCompletePreference, 0, 0)

        entries = TypedArrayUtils.getTextArray(a,
            R.styleable.AutoCompletePreference_entries,
            R.styleable.AutoCompletePreference_entries) ?: arrayOf()

        if (TypedArrayUtils.getBoolean(a,
                R.styleable.AutoCompletePreference_useSimpleSummaryProvider,
                R.styleable.AutoCompletePreference_useSimpleSummaryProvider, false)){
            summaryProvider = SimpleSummaryProvider
        }

        a.recycle()

    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        if (isPersistent) {
            return superState
        }
        val myState = SavedState(superState)

        myState.value = text
        return myState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {

        if (state == null || state.javaClass != SavedState::class.java) {
            super.onRestoreInstanceState(state)
            return
        }

        val myState = state as SavedState
        super.onRestoreInstanceState(myState.superState)
        text = myState.value ?: EMPTY_VALUE
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        if (restorePersistedValue) {
            text = getPersistedString(defaultValue as String? ?: EMPTY_VALUE)
        } else {
            text = defaultValue as String? ?: EMPTY_VALUE
            persistString(text)
        }
    }

    override fun onGetDefaultValue(a: TypedArray?, index: Int) = a?.getString(index) ?: ""

    override fun getSummary(): CharSequence? = SimpleSummaryProvider.provideSummary(this)

    override fun setSummary(summary: CharSequence?) = run { mSummary = summary }

    private class SavedState : BaseSavedState {

        internal var value: String? = null

        constructor(superState: Parcelable) : super(superState)
        constructor(source: Parcel) : super(source) {
            value = source.readString()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeString(value)
        }

        companion object {

            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    object SimpleSummaryProvider: SummaryProvider<AutoCompletePreference> {

        override fun provideSummary(preference: AutoCompletePreference): CharSequence? {
            return if (TextUtils.isEmpty(preference.text)) {
                preference.context.getString(R.string.not_setted)
            } else {
                preference.text
            }
        }
    }

}