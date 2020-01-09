package iv.nakonechnyi.preferancepresentation.preferences

import android.content.Context
import android.content.res.TypedArray
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import androidx.preference.DialogPreference
import iv.nakonechnyi.preferancepresentation.R

class DatePreference(context: Context, attrs: AttributeSet? = null
) : DialogPreference(context, attrs) {

    init {
        summaryProvider = SimpleSummaryProvider
    }

    var date: String? = null
        set(value) {
            val changed = !TextUtils.equals(value, date)
            if (changed) {
                field = value
                persistString(value)
                notifyChanged()
            }
        }

    private var mSummary: String? = null

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        if (isPersistent) {
            return superState
        }
        val myState = SavedState(superState)

        myState.value = date
        return myState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {

        if (state == null || state.javaClass != SavedState::class.java) {
            super.onRestoreInstanceState(state)
            return
        }

        val myState = state as SavedState
        super.onRestoreInstanceState(myState.superState)
        date = myState.value
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        if (restorePersistedValue) {
            date = getPersistedString(null)
        } else {
            date = defaultValue as String
            persistString(date)
        }
    }

    override fun onGetDefaultValue(a: TypedArray?, index: Int) = a?.getString(index)

    override fun setSummary(summary: CharSequence?) = run { mSummary = summary.toString() }

    override fun getSummary(): CharSequence? = summaryProvider?.provideSummary(this)



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

    object SimpleSummaryProvider: SummaryProvider<DatePreference> {

        override fun provideSummary(preference: DatePreference): CharSequence? {
            return if (TextUtils.isEmpty(preference.date)) {
                preference.context.getString(R.string.not_setted)
            } else {
                preference.date
            }
        }
    }

}