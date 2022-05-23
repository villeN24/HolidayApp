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

class MyAdapter(private val context : Activity, private val holidayList : MutableList<Holiday>) : ArrayAdapter<Holiday>(context, R.layout.holidaylist_item, holidayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row : View

        if (convertView == null) {
            val inflater : LayoutInflater = LayoutInflater.from(context)
            row = inflater.inflate(R.layout.holidaylist_item, null)
        } else {
            row = convertView
        }

        var shape : GradientDrawable = ContextCompat.getDrawable(context, R.drawable.holidayitem_bg) as GradientDrawable
        shape.mutate()
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
        type.text = "${typeList.joinToString("\n")}"
        val locationsList = holidayList[position].locations?.split(",")?.toTypedArray()
        locations.text = holidayList[position].locations?.replace(", ", "\n")

        return row
    }

}