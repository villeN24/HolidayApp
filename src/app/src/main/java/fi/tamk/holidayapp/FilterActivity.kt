package fi.tamk.holidayapp

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text

class FilterActivity : AppCompatActivity() {
    lateinit var monthTitle : TextView
    lateinit var monthPicker : NumberPicker
    lateinit var dayTitle : TextView
    lateinit var dayPicker : NumberPicker
    lateinit var yearTitle : TextView
    lateinit var yearPicker : NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        var dayList = mutableListOf<String>("All")
        for (i in 1 .. 31) {
            dayList.add(i.toString())
        }
        this.dayTitle = findViewById(R.id.dayTitle)
        this.dayPicker = findViewById(R.id.dayPicker)
        dayPicker.minValue = 0
        dayPicker.maxValue = 31
        dayPicker.displayedValues = dayList.toTypedArray()

        val months = resources.getStringArray(R.array.months)
        this.monthTitle = findViewById(R.id.monthTitle)
        this.monthPicker = findViewById(R.id.monthPicker)
        monthPicker.minValue = 0
        monthPicker.maxValue = 12
        monthPicker.displayedValues = months

        monthPicker.setOnValueChangedListener { monthPicker, oldVal, newVal ->
            monthTitle.text = newVal.toString()
        }

        this.yearTitle = findViewById(R.id.yearTitle)
        this.yearPicker = findViewById(R.id.yearPicker)
        yearPicker.minValue = 1970
        yearPicker.maxValue = 2049
        yearPicker.value = 2022
    }
}
