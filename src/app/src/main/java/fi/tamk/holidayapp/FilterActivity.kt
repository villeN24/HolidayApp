package fi.tamk.holidayapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.*

/**
 * The activity for adding filters.
 *
 * An activity, that includes multiple scrollabe selectors that
 * are used for selecting additional parameters. These parameters
 * are used to filter the list of holidays if selected.
 */
class FilterActivity : AppCompatActivity() {
    lateinit var monthTitle : TextView
    lateinit var monthPicker : NumberPicker
    lateinit var dayTitle : TextView
    lateinit var dayPicker : NumberPicker
    lateinit var yearTitle : TextView
    lateinit var yearPicker : NumberPicker
    lateinit var categoryTitle : TextView
    lateinit var categoryPicker : NumberPicker
    lateinit var hintButton : ImageButton
    lateinit var hintText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        // Create a number picker with one string choice
        // for choosing a day or all.
        var dayList = mutableListOf("All")
        for (i in 1 .. 31) {
            dayList.add(i.toString())
        }
        this.dayTitle = findViewById(R.id.dayTitle)
        this.dayPicker = findViewById(R.id.dayPicker)
        dayPicker.minValue = 0
        dayPicker.maxValue = 31
        dayPicker.displayedValues = dayList.toTypedArray()

        // Create a number picker of displayed string values
        // with a string resource file for choosing a month or all.
        val months = resources.getStringArray(R.array.months)
        this.monthTitle = findViewById(R.id.monthTitle)
        this.monthPicker = findViewById(R.id.monthPicker)
        monthPicker.minValue = 0
        monthPicker.maxValue = 12
        monthPicker.displayedValues = months

        // Create a number picker for selecting a year.
        this.yearTitle = findViewById(R.id.yearTitle)
        this.yearPicker = findViewById(R.id.yearPicker)
        yearPicker.minValue = 1970
        yearPicker.maxValue = 2049

        // Create a number picker of displayed string values
        // with a string resource file for choosing a category or all.
        val categories = resources.getStringArray(R.array.categories)
        this.categoryTitle = findViewById(R.id.categoryTitle)
        this.categoryPicker = findViewById(R.id.categoryPicker)
        categoryPicker.minValue = 0
        categoryPicker.maxValue = 4
        categoryPicker.displayedValues = categories

        // Create a button, and set a click listener on it. If triggered,
        // creates a card dialog with text to be displayed.
        this.hintButton = findViewById(R.id.hintButton)
        hintButton.setOnClickListener {
            val view = View.inflate(this, R.layout.hint_alert, null)
            this.hintText = view.findViewById(R.id.generalInst)
            val builder = AlertDialog.Builder(this)
            builder.setView(view)

            val dialog = builder.create()
            dialog.show()

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

        // Set number picker starting values if given
        val extras : Bundle? = intent.extras
        dayPicker.value = extras?.getString("day")?.toInt() ?: 0
        monthPicker.value = extras?.getString("month")?.toInt() ?: 0
        yearPicker.value = extras?.getString("year")?.toInt() ?: Calendar.getInstance().get(Calendar.YEAR)
        categoryPicker.value = extras?.getString("type")?.toInt() ?: 0
    }

    /**
     * Overrides the onBackPressed function.
     *
     * Detects if the user presses a button to return to
     * an earlier view. Creates an intent with data, returns
     * said intent, and triggers an animation before returning
     * to an earlier activity.
     */
    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("day", dayPicker.value.toString())
        intent.putExtra("month", monthPicker.value.toString())
        intent.putExtra("year", yearPicker.value.toString())
        intent.putExtra("type", categoryPicker.value.toString())
        setResult(RESULT_OK, intent);
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_top,
            R.anim.slide_out_bottom)
    }

    /**
     * Saves data if the OS kills the activity.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("day", dayPicker.value)
        outState.putInt("month", monthPicker.value)
        outState.putInt("year", yearPicker.value)
        outState.putInt("category", categoryPicker.value)
        super.onSaveInstanceState(outState)
    }

    /**
     * Restored data saved in onSaveInstance if it was triggered.
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        dayPicker.value = savedInstanceState.getInt("day")
        monthPicker.value = savedInstanceState.getInt("month")
        yearPicker.value = savedInstanceState.getInt("year")
        categoryPicker.value = savedInstanceState.getInt("category")
    }
}
