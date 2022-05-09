package fi.tamk.holidayapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HolidayList : AppCompatActivity() {
    lateinit var selectedCountry : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holidaylist)

        this.selectedCountry = findViewById(R.id.selectedCountry)
        val extras : Bundle? = intent.extras
        if (extras != null) {
            selectedCountry.text = "${extras.getString("country")} ${extras.getString("code")}"
            "https://calendarific.com/api/v2/holidays?&api_key=82412897a0bd6afebfd64c44eab3013ba5c88a52&country=US"

        }
    }
}