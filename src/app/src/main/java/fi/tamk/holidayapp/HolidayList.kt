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
            selectedCountry.text = extras.getString("country")
        }
    }
}