package fi.tamk.holidayapp

import android.content.Context
import android.util.Log
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.thread

val API_KEY = "82412897a0bd6afebfd64c44eab3013ba5c88a52"

// Needed Jackson parser helper classes for parsing
// the api call containing all of the available countries
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

// Needed Jackson parser helper classes for parsing the api
// call all the holidays and their infos of a single country
@JsonIgnoreProperties(ignoreUnknown  = true)
data class ResponseObjectHolidays(var response : HolidayListObj? = null)

@JsonIgnoreProperties(ignoreUnknown  = true)
data class HolidayListObj(var holidays : MutableList<Holiday>? = null)

@JsonIgnoreProperties(ignoreUnknown  = true)
data class Holiday(var name : String? = null,
                   var description : String? = null,
                   var date : HolidayDate? = null,
                   var type : MutableList<HolidayType>? = null,
                    var locations: String? = null)

@JsonIgnoreProperties(ignoreUnknown  = true)
data class HolidayDate(var datetime : HolidayDateTime? = null)

@JsonIgnoreProperties(ignoreUnknown  = true)
data class HolidayDateTime(var year : Int? = null, var month : Int? = null, var day : Int? = null, var hour : Int? = null, var minute : Int? = null, var second : Int? = null)

@JsonIgnoreProperties(ignoreUnknown  = true)
data class HolidayType(var type : String? = null)

/**
 * Fetches, formats and returns all available countries.
 *
 * Calls an url and receives a json object of data, deconstructs it
 * to a kotlin friendly format, and returns formatted data.
 *
 * @param context The context of the call.
 * @param callback A callback function to return the formatted data.
 */
fun fetchCountryList(context : Context, callback : (data : MutableList<Country>?) -> Unit) {
    thread {
        val json : String? = getUrl("https://calendarific.com/api/v2/countries?api_key=${API_KEY}")
        val response : ResponseObjectCountries = ObjectMapper().readValue(json, ResponseObjectCountries::class.java)
        val responseList : CountryList? = response.response
        val countries : MutableList<Country>? = responseList?.countries
        callback(countries)
    }
}

/**
 * Fetches, formats and returns all required holidays.
 *
 * Calls an url with given filter parameters and receives a json
 * object containing data, formats it to a kotlin-friendly format.
 * Formats this data and returns it.
 *
 * @param context The context of the call.
 * @param countryCode Required parameter that determines which country's
 * holidays are to be returned. - Required
 * @param day A parameter to filter results to a certain day. - Optional
 * @param month Optional parameter to filter results to a certain month. - Optional
 * @param year Required parameter that determines which year's holidays - Required
 * are to be returned.
 * @param type An optional parameter to filter results to a certain category. - Optional
 * @param futureOnly A parameter that determines if holidays in history are to
 * be included. - Optional
 * @param callback A callback function to return the data.
 */
fun fetchHolidayList(context : Context, countryCode : String?, day : String?, month : String?, year : String?, type : String?, futureOnly : Boolean, callback : (data : MutableList<Holiday>?) -> Unit) {
    thread {
        // Variables that are formatted to a format that can be added to the api call.
        var mYear : String = year ?: Calendar.getInstance().get(Calendar.YEAR).toString()
        var mDay : String? = if ( day != null && day.toString() != "0" ) "&day=${day}" else ""
        var mMonth : String? = if ( month != null && month.toString() != "0" ) "&month=${month}" else ""
        var mType = when (type) {
            "1" -> "&type=national"
            "2" -> "&type=local"
            "3" -> "&type=religious"
            "4" -> "&type=observance"
            else -> ""
        }
        val json : String? = getUrl("https://calendarific.com/api/v2/holidays?&api_key=${API_KEY}&country=${countryCode}&year=${mYear}${mDay}${mMonth}${mType}")
        Log.d("MyUtils", "https://calendarific.com/api/v2/holidays?&api_key=${API_KEY}&country=${countryCode}&year=${mYear}${mDay}${mMonth}${mType}")
        val response : ResponseObjectHolidays = ObjectMapper().readValue(json, ResponseObjectHolidays::class.java)
        val holidayList : HolidayListObj? = response.response
        var holidays : MutableList<Holiday>? = holidayList?.holidays

        // If futureOnly is true, filters out holidays that are in history.
        if(futureOnly) {
            Log.d("MyUtils", "In futureOnly")
            val isoDate : Int = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE).toInt()
            var filteredHolidays : MutableList<Holiday>? = holidays?.filter {
                it.convertToBasicIsoDate().toInt() > isoDate
            } as MutableList<Holiday>?
            holidays = filteredHolidays
        }
        callback(holidays)
    }
}

/**
 * Converts a holiday objects date to a string.
 *
 * An extension function to a Holiday object, that parses
 * objects date variables to a usable string format.
 *
 * @return A date formatted to a string form.
 */
fun Holiday.convertToBasicIsoDate() : String {
    val year = this.date?.datetime?.year
    val month = String.format("%02d",this.date?.datetime?.month)
    val day = String.format("%02d",this.date?.datetime?.day)
    return "$year$month$day"
}

/**
 * Fetches and url and returns the result.
 *
 * @param url The url that is called.
 *
 * @return Returns the formatted data.
 */
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

/**
 * Converts Holiday objects type to a usable format.
 *
 * An extension function to a Holiday object, that converts
 * all known variations of categories, and formats them to
 * more generic types.
 *
 * @return A generalized type in string format.
 */
fun Holiday.getHolidayType() : String? {
    var holiday : String? = this.type?.get(0)?.type?.lowercase()

    if (holiday != null) {
        if (holiday.contains("national")
            || holiday.contains("bank")) return "national"

        if (holiday.contains("local")) return "local"

        if (holiday.contains("season")
            || holiday.contains("observance")
            || holiday.contains("times")
            || holiday.contains("clock")
            || holiday.contains("sport")) return "season"

        if( holiday.contains("christ")
            || holiday.contains("ortho")
            || holiday.contains("hebrew")
            || holiday.contains("jewish")
            || holiday.contains("hindu")
            || holiday.contains("muslim")) return "religious"
    }
    return null
}