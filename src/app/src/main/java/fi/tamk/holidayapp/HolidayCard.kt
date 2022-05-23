package fi.tamk.holidayapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class HolidayCard : AppCompatActivity() {
    lateinit var descView : TextView
    lateinit var dateView : TextView
    lateinit var typeView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.holiday_card)

        this.descView = findViewById(R.id.desc)
        this.dateView = findViewById(R.id.date)
        this.typeView = findViewById(R.id.type)

        val holiday = intent.getSerializableExtra("holiday") as Holiday
        title = holiday.name
        descView.text = holiday.description
        dateView.text = "${ holiday.date?.datetime?.day }.${ holiday.date?.datetime?.month }.${ holiday.date?.datetime?.year } "
        var typeList : ArrayList<String> = arrayListOf()
        holiday.type?.forEach {
            typeList.add(it.type.toString())
        }
        typeView.text = "Categories: ${typeList.joinToString(", ")}"
        if (holiday.date?.datetime?.hour != null) {
            dateView.text = dateView.text.toString() + "${ holiday.date?.datetime?.hour }:${ holiday.date?.datetime?.minute }:${ holiday.date?.datetime?.second } "
        }
    }
}