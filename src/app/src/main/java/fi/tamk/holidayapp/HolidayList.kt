package fi.tamk.holidayapp

import android.app.Activity
import android.content.Context
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

//        val extras : Bundle? = intent.extras
//        selectedCountry.text = "${extras.getString("country")} ${extras.getString("code")}"
        fetchHolidayList(this, "FI",
            "2022") {
            Log.d("HolidayActivity", it.toString())
            if (it != null) {
                for (item : Holiday in it) {
                    Log.d("HolidayList", item.toString())
                    runOnUiThread {
                        for (item : Holiday in it) {
                            holidayAdapter.add(item)
                        }
                    }
                }
            }
        }
    }
}
class MyAdapter(private val context : Activity, private val holidayList : MutableList<Holiday>) : ArrayAdapter<Holiday>(context, R.layout.holidaylist_item, holidayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.holidaylist_item, null)

        var name : TextView = view.findViewById(R.id.name)
        var desc : TextView = view.findViewById(R.id.desc)

        name.text = holidayList[position].name
        desc.text = holidayList[position].description


        return view
    }

}


