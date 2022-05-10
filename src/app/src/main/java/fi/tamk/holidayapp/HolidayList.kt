package fi.tamk.holidayapp

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HolidayList : AppCompatActivity() {
    lateinit var selectedCountry : TextView
    lateinit var holidayList : ListView
    lateinit var adapter : ArrayAdapter<Holiday>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holidaylist)
        this.holidayList = findViewById(R.id.holidayList)
        adapter = ArrayAdapter<Holiday>(this, R.layout.holidaylist_item, R.id.myTextView, mutableListOf<Holiday>())
        holidayList.adapter = adapter

        this.selectedCountry = findViewById(R.id.selectedCountry)
//        val extras : Bundle? = intent.extras
//        if (extras != null) {
//            selectedCountry.text = "${extras.getString("country")} ${extras.getString("code")}"
//            Log.d("HolidayActivity", extras.toString())
//            fetchHolidayList(this, extras.getString("code"), "2022") {
//                Log.d("HolidayActivity", it.toString())
//            }
//        }
        fetchHolidayList(this, "FI", "2022") {
            Log.d("HolidayActivity", it.toString())
        }
    }
}