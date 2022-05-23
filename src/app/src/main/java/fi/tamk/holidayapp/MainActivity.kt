package fi.tamk.holidayapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener

class MainActivity : AppCompatActivity() {
    lateinit var spinner : Spinner
    lateinit var adapter : ArrayAdapter<Country>
    lateinit var filterList : EditText
    lateinit var seeHolidays : Button
    lateinit var seeFilter : Button
    var countryList = mutableListOf<Country>()
    var selectedCountry : Country? = null
    var day : String? = null
    var month : String? = null
    var year : String? = null
    var type : String? = null
    var locations : String? = null
    lateinit var futureSwitch : Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.spinner = findViewById(R.id.spinnerList)
        this.filterList = findViewById(R.id.filterList)
        this.seeHolidays = findViewById(R.id.seeHolidays)
        this.futureSwitch = findViewById(R.id.futureSwitch)
        this.seeFilter = findViewById(R.id.seeFilters)
        this.filterList.addTextChangedListener { updateList() }
        fetchCountryList(this) {
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
        intent.putExtra("day", day)
        intent.putExtra("month", month)
        intent.putExtra("year", year)
        intent.putExtra("type", type)
        intent.putExtra("locations", locations)
        intent.putExtra("futureOnly", futureSwitch.isChecked)
        startActivity(intent)
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data : Intent? = it.data
            day = data?.getStringExtra("day")
            month = data?.getStringExtra("month")
            year = data?.getStringExtra("year")
            type = data?.getStringExtra("type")
        }
    }

    fun moveToFilterScreen(seeFilters : View) {
        val intent = Intent(this, FilterActivity::class.java)
        intent.putExtra("day", day)
        intent.putExtra("month", month)
        intent.putExtra("year", year)
        intent.putExtra("type", type)
        resultLauncher.launch(intent)
        overridePendingTransition(R.anim.slide_in_bottom,
            R.anim.slide_out_top)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("day", day)
        outState.putString("month", month)
        outState.putString("year", year)
        outState.putString("category", type)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        day = savedInstanceState.getString("day")
        month = savedInstanceState.getString("month")
        year = savedInstanceState.getString("year")
        type = savedInstanceState.getString("category")
    }
}