package fi.tamk.holidayapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import fi.tamk.holidayapp.databinding.ActivityMainBinding

class HolidayList : AppCompatActivity() {
    lateinit var holidayList : ListView
    lateinit var holidayAdapter : MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holidaylist)

        this.holidayList = findViewById(R.id.holidayList)
        holidayAdapter = MyAdapter(this, mutableListOf<Holiday>())
        holidayList.adapter = holidayAdapter
        holidayList.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, HolidayCard::class.java)
            intent.putExtra("holiday", holidayAdapter.getItem(position))
            startActivity(intent)
        }

        val extras : Bundle? = intent.extras
//        selectedCountry.text = "${extras.getString("country")} ${extras.getString("code")}"
//        title = extras?.getString("country")
        fetchHolidayList(this, "FI",
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
class MyAdapter(private val context : Activity, private val holidayList : MutableList<Holiday>) : ArrayAdapter<Holiday>(context, R.layout.holidaylist_item, holidayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row : View

        if (convertView == null) {
            val inflater : LayoutInflater = LayoutInflater.from(context)
            row = inflater.inflate(R.layout.holidaylist_item, null)
        } else {
            row = convertView
        }

        var name : TextView = row.findViewById(R.id.name)
        var desc : TextView = row.findViewById(R.id.desc)
        var date : TextView = row.findViewById(R.id.date)
        var type : TextView = row.findViewById(R.id.type)

        name.text = holidayList[position].name
        desc.text = holidayList[position].description
        date.text = "${holidayList[position].date?.datetime?.day}." +
                "${holidayList[position].date?.datetime?.month}." +
                "${holidayList[position].date?.datetime?.year}"

        var typeList : ArrayList<String> = arrayListOf()
        holidayList[position].type?.forEach {
            typeList.add(it.type.toString())
        }
        type.text = "Categories: ${typeList.joinToString(", ")}"

        return row
    }

}


