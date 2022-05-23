package fi.tamk.holidayapp

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.w3c.dom.Text

/**
 * The activity for displaying a list of holidays.
 *
 * Displays a list of holidays of the country of choosing.
 * Includes rows color coded by category of the holiday, and
 * a card view to display additional info when clicking a row.
 */
class HolidayList : AppCompatActivity() {
    lateinit var holidayList : ListView
    lateinit var holidayAdapter : MyAdapter
    lateinit var name : TextView
    lateinit var desc : TextView
    lateinit var date : TextView
    lateinit var type : TextView
    lateinit var locations : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holidaylist)

        this.holidayList = findViewById(R.id.holidayList)
        holidayAdapter = MyAdapter(this, mutableListOf<Holiday>())
        holidayList.adapter = holidayAdapter
        // Create a listener for a row item, and trigger a card dialog
        // containing additional info when clicked.
        holidayList.setOnItemClickListener { parent, view, position, id ->
            val view = View.inflate(this, R.layout.holiday_card, null)
            this.name = view.findViewById(R.id.name)
            this.desc = view.findViewById(R.id.desc)
            this.date = view.findViewById(R.id.date)
            this.type = view.findViewById(R.id.type)
            this.locations = view.findViewById(R.id.locations)
            val builder = AlertDialog.Builder(this)
            builder.setView(view)

            val dialog = builder.create()
            dialog.show()

            val holiday = holidayAdapter.getItem(position)
            name.text = holiday?.name
            desc.text = holiday?.description
            date.text = "${holiday?.date?.datetime?.day}." +
                    "${holiday?.date?.datetime?.month}." +
                    "${holiday?.date?.datetime?.year}"

            var typeList : ArrayList<String> = arrayListOf()
            holiday?.type?.forEach {
                typeList.add(it.type.toString())
            }
            // Joins an array of strings into a one string with line breaks
            // for ease of displaying.
            type.text = "${typeList.joinToString("\n")}"
            // Finds strings separated by ", " and replaces them with line breaks
            // for ease of displaying.
            locations.text = holiday?.locations?.replace(", ", "\n")

            builder.setPositiveButton("OK") { dialogInterface : DialogInterface, i : Int ->
                finish()
            }
        }

        val extras : Bundle? = intent.extras
        // Change the title of the screen to the selected country.
        if (extras?.getString("country") != null) title = extras.getString("country")
        title = extras?.getString("country")

        // Fetches the list of holidays with given arguments, and
        // creates an adapter with them to display the info.
        fetchHolidayList(this, extras?.getString("code"),
            extras?.getString("day"), extras?.getString("month"), extras?.getString("year"), extras?.getString("type"), extras?.getBoolean("futureOnly") ?: false) {
            if (it != null) {
                for (item : Holiday in it) {
                    runOnUiThread {
                        holidayAdapter.add(item)
                    }
                }
            }
        }
    }
}


