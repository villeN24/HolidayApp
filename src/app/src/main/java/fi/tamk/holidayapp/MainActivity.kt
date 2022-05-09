package fi.tamk.holidayapp

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener

val allCountries = "https://calendarific.com/api/v2/countries?api_key=82412897a0bd6afebfd64c44eab3013ba5c88a52"

class MainActivity : AppCompatActivity() {
    lateinit var spinner : Spinner
    lateinit var adapter : ArrayAdapter<Country>
    lateinit var filterList : EditText
    lateinit var seeHolidays : Button
    var countryList = mutableListOf<Country>()
    var selectedCountry : Country? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.spinner = findViewById(R.id.spinnerList)
        this.filterList = findViewById(R.id.filterList)
        this.seeHolidays = findViewById(R.id.seeHolidays)
        this.filterList.addTextChangedListener { updateList() }
        fetchCountryList(this, "https://calendarific.com/api/v2/countries?api_key=82412897a0bd6afebfd64c44eab3013ba5c88a52") {
            if (it != null) countryList = it
            updateList()
        }
        this.spinner.onItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedCountry = spinner.selectedItem as Country?
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
    private fun updateList() {
        if (countryList != null) {
            var updatedList = countryList.filter { it.country_name!!.contains(filterList.text.toString(), ignoreCase = true) }
            adapter = ArrayAdapter<Country>(
                this,
                R.layout.list_item,
                updatedList)
            runOnUiThread {
                this.spinner.adapter = adapter
            }
        }
    }
    fun moveToHolidays(seeHolidays: View) {
        val intent = Intent(this, HolidayList::class.java)
        intent.putExtra("country", selectedCountry?.country_name)
        intent.putExtra("code", selectedCountry?.countryCode)
        startActivity(intent)
    }
}