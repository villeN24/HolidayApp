package fi.tamk.holidayapp

import android.content.ClipDescription
import android.content.Context
import android.util.Log
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

val API_KEY = "82412897a0bd6afebfd64c44eab3013ba5c88a52"

// Country listview
@JsonIgnoreProperties(ignoreUnknown  = true)
data class ResponseObjectCountries(var response : CountryList? = null)

@JsonIgnoreProperties(ignoreUnknown  = true)
data class CountryList(var countries : MutableList<Country>? = null)

@JsonIgnoreProperties(ignoreUnknown  = true)
data class Country(
    @JsonProperty("country_name") var country_name : String? = null,
    @JsonProperty("iso-3166") var countryCode : String? = null) {
    override fun toString(): String {
        return country_name ?: ""
    }
}

// Holiday listview
@JsonIgnoreProperties(ignoreUnknown  = true)
data class ResponseObjectHolidays(var response : HolidayListObj? = null)

@JsonIgnoreProperties(ignoreUnknown  = true)
data class HolidayListObj(var holidays : MutableList<Holiday>? = null)

@JsonIgnoreProperties(ignoreUnknown  = true)
data class Holiday(var name : String? = null, var description : String? = null, var date : HolidayDate? = null, var type : MutableList<HolidayType>? = null)

@JsonIgnoreProperties(ignoreUnknown  = true)
data class HolidayDate(var datetime : HolidayDateTime? = null)

@JsonIgnoreProperties(ignoreUnknown  = true)
data class HolidayDateTime(var year : Int? = null, var month : Int? = null, var day : Int? = null, var hour : Int? = null, var minute : Int? = null, var second : Int? = null)

@JsonIgnoreProperties(ignoreUnknown  = true)
data class HolidayType(var type : String? = null)

fun fetchCountryList(context : Context, callback : (data : MutableList<Country>?) -> Unit) {
    thread {
        val json : String? = getUrl("https://calendarific.com/api/v2/countries?api_key=${API_KEY}")
        val response : ResponseObjectCountries = ObjectMapper().readValue(json, ResponseObjectCountries::class.java)
        val responseList : CountryList? = response.response
        val countries : MutableList<Country>? = responseList?.countries
        Log.d("MyUtils", countries.toString())
        callback(countries)
    }
}

fun fetchHolidayList(context : Context, countryCode : String?, year : String?, callback : (data : MutableList<Holiday>?) -> Unit) {
    thread {
        val json : String? = getUrl("https://calendarific.com/api/v2/holidays?&api_key=${API_KEY}&country=${countryCode}&year=${year}")
        val response : ResponseObjectHolidays = ObjectMapper().readValue(json, ResponseObjectHolidays::class.java)
        val holidayList : HolidayListObj? = response.response
        val holidays : MutableList<Holiday>? = holidayList?.holidays
        Log.d("MyUtils", json.toString())
        Log.d("MyUtils", response.toString())
        Log.d("MyUtils", holidays?.get(0).toString())
        callback(holidays)
    }
}


fun getUrl(url : String) : String? {
    var result : String? = null
    val sb = StringBuffer()
    val myUrl = URL(url)
    val conn = myUrl.openConnection() as HttpURLConnection
    val reader = BufferedReader(InputStreamReader(conn.inputStream))

    reader.use {
        var linja : String? = null
        do {
            linja = it.readLine()
            sb.append(linja)
        } while (linja != null)
        result = sb.toString()
    }
    return result
}
