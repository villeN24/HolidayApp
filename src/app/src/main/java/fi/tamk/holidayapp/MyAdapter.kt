package fi.tamk.holidayapp

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 * A custom class for displaying the holiday list.
 *
 * A class that extends basic ArrayAdapter class. Was
 * created in order to populate holiday list with more
 * complex views than the normal adapter allows.
 */
class MyAdapter(private val context : Activity, private val holidayList : MutableList<Holiday>) : ArrayAdapter<Holiday>(context, R.layout.holidaylist_item, holidayList) {

    /**
     * Creates and returns a view for displaying.
     *
     * Overridden function in order to create more complex
     * views to return, and to add additional logic to the function.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row : View

        if (convertView == null) {
            val inflater : LayoutInflater = LayoutInflater.from(context)
            row = inflater.inflate(R.layout.holidaylist_item, null)
        } else {
            row = convertView
        }

        // Create a drawable in order to paint border to the view.
        var shape : GradientDrawable = ContextCompat.getDrawable(context, R.drawable.holidayitem_bg) as GradientDrawable
        shape.mutate()
        // Color codes the row border with the corresponding category.
        when(holidayList[position].getHolidayType()) {
            "national" -> shape.setStroke(4, ContextCompat.getColor(context, R.color.national))
            "religious" -> shape.setStroke(4, ContextCompat.getColor(context, R.color.religious))
            "local" -> shape.setStroke(4, ContextCompat.getColor(context, R.color.local))
            "season" -> shape.setStroke(4, ContextCompat.getColor(context, R.color.observance))
            else -> shape.setStroke(4, Color.WHITE)
        }
        row.background = shape

        var name : TextView = row.findViewById(R.id.name)
        var date : TextView = row.findViewById(R.id.date)
        var type : TextView = row.findViewById(R.id.type)
        var locations : TextView = row.findViewById(R.id.locations)

        name.text = holidayList[position].name
        date.text = "${holidayList[position].date?.datetime?.day}." +
                "${holidayList[position].date?.datetime?.month}." +
                "${holidayList[position].date?.datetime?.year}"

        var typeList : ArrayList<String> = arrayListOf()
        holidayList[position].type?.forEach {
            typeList.add(it.type.toString())
        }
        // Joins an array of strings into a one string with line breaks
        // for ease of displaying.
        type.text = "${typeList.joinToString("\n")}"

        // Replaces separators with line breaks for ease of displaying.
        locations.text = holidayList[position].locations?.replace(", ", "\n")

        return row
    }

}