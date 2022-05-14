package fi.tamk.holidayapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class HolidayCard : AppCompatActivity() {
    lateinit var nameView : TextView
    lateinit var descView : TextView
    lateinit var dateView : TextView
    lateinit var typeView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holiday_card)

        this.nameView = findViewById(R.id.name)
        this.descView = findViewById(R.id.desc)
        this.dateView = findViewById(R.id.date)
        this.typeView = findViewById(R.id.type)

        val extras : Bundle? = intent.extras
        nameView.text = extras?.getString("name")
        descView.text = extras?.getString("desc")
        dateView.text = extras?.getString("date")
        typeView.text = extras?.getString("type")
    }
}