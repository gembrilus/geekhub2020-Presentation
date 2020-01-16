package iv.nakonechnyi.preferancepresentation.preferences

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import iv.nakonechnyi.preferancepresentation.R
import kotlinx.android.synthetic.main.preference_color_picker.view.*

class ColorPickerPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs), View.OnClickListener{

    companion object {

        private val DEFAULT_VALUE = Color.RED

    }

    var color: Int = DEFAULT_VALUE
        set(value) {
            if (value != color) {
                field = value
                persistInt(value)
                notifyChanged()
            }
        }

    init {

        widgetLayoutResource = R.layout.preference_color_picker
        isSelectable = false

    }

    override fun onSetInitialValue(defaultValue: Any?) {
            color = getPersistedInt(defaultValue as Int? ?: DEFAULT_VALUE)
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any = a.getColor(index, Color.RED)

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        with(holder.itemView) {
            red.setOnClickListener(this@ColorPickerPreference)
            blue.setOnClickListener(this@ColorPickerPreference)
            green.setOnClickListener(this@ColorPickerPreference)
            yellow.setOnClickListener(this@ColorPickerPreference)
        }

    }

    override fun onClick(v: View) {
        val colorDrawable = v.background as ColorDrawable
        if(callChangeListener(color)){
            color = colorDrawable.color
        }
    }
}