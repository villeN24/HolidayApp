package fi.tamk.holidayapp

import android.content.Context
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import androidx.core.widget.addTextChangedListener

val allCountries = "https://calendarific.com/api/v2/countries?api_key=82412897a0bd6afebfd64c44eab3013ba5c88a52"

class MainActivity : AppCompatActivity() {
    lateinit var spinner : Spinner
    lateinit var adapter : ArrayAdapter<String>
    lateinit var filterList : EditText
    var testList = mutableListOf("matti", "maali", "timppa", "pauli")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.spinner = findViewById(R.id.spinnerList)
        adapter = ArrayAdapter<String>(
            this,
            R.layout.list_item,
            testList)
        this.spinner.adapter = adapter
        this.filterList = findViewById(R.id.filterList)
        this.filterList.addTextChangedListener { updateList() }
    }
    private fun updateList() {
        val updatedList = testList.filter { it.contains(filterList.text.toString(), ignoreCase = true) }
        adapter = ArrayAdapter<String>(
            this,
            R.layout.list_item,
            updatedList)
        this.spinner.adapter = adapter
    }
}