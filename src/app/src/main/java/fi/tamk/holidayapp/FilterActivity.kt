package fi.tamk.holidayapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text

class FilterActivity : AppCompatActivity() {
    lateinit var monthTitle : TextView
    lateinit var monthPicker : NumberPicker
    lateinit var dayTitle : TextView
    lateinit var dayPicker : NumberPicker
    lateinit var yearTitle : TextView
    lateinit var yearPicker : NumberPicker
    lateinit var categoryTitle : TextView
    lateinit var categoryPicker : NumberPicker
    lateinit var hintButton : Button
    lateinit var hintText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        var dayList = mutableListOf("All")
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

        this.yearTitle = findViewById(R.id.yearTitle)
        this.yearPicker = findViewById(R.id.yearPicker)
        yearPicker.minValue = 1970
        yearPicker.maxValue = 2049
        yearPicker.value = 2022

        val categories = resources.getStringArray(R.array.categories)
        this.categoryTitle = findViewById(R.id.categoryTitle)
        this.categoryPicker = findViewById(R.id.categoryPicker)
        categoryPicker.minValue = 0
        categoryPicker.maxValue = 4
        categoryPicker.displayedValues = categories


        this.hintButton = findViewById(R.id.hintButton)
        hintButton.setOnClickListener {

            val view = View.inflate(this, R.layout.hint_alert, null)
            this.hintText = view.findViewById(R.id.generalInst)
            val builder = AlertDialog.Builder(this)
            builder.setView(view)

            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            hintText.text = """
            When filtering using any of the values, the search
            will include results that fulfill every condition
            only. You can select any combination of filters to use.

            Category explanations:

            - National
              Returns public, federal and bank holidays
            
            - Local
              Returns local, regional and state holidays
            
            - Religious
              Return religious holidays: buddhism, christian,
              hinduism, muslim, etc
            
            - Observance
              Observance, Seasons, Times
        """.trimIndent()
            builder.setPositiveButton("OK") { dialogInterface : DialogInterface, i : Int ->
                finish()
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("day", dayPicker.value.toString())
        intent.putExtra("month", monthPicker.value.toString())
        intent.putExtra("year", yearPicker.value.toString())
        intent.putExtra("type", categoryPicker.value.toString())
        setResult(RESULT_OK, intent);
        super.onBackPressed()
    }
}
