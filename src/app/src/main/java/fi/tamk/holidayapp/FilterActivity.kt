package fi.tamk.holidayapp

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text

class FilterActivity : AppCompatActivity() {
    lateinit var monthTitle : TextView
    lateinit var monthSpinner : Spinner
    lateinit var monthPicker : NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        val months = resources.getStringArray(R.array.months)
        this.monthTitle = findViewById(R.id.monthTitle)
        this.monthPicker = findViewById(R.id.monthPicker)
        monthPicker.minValue = 0
        monthPicker.maxValue = 12
        monthPicker.displayedValues = months

        monthPicker.setOnValueChangedListener { monthPicker, oldVal, newVal ->
            monthTitle.text = newVal.toString()
        }

    }
}
