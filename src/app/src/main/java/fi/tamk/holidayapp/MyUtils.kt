package fi.tamk.holidayapp

import android.content.Context
import android.util.Log
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

@JsonIgnoreProperties(ignoreUnknown  = true)
data class ResponseObject(var response : ResponseList? = null)

@JsonIgnoreProperties(ignoreUnknown  = true)
data class ResponseList(var countries : MutableList<Country>? = null)

@JsonIgnoreProperties(ignoreUnknown  = true)
data class Country(var country_name : String? = null)

fun downloadUrlAsync(context : Context, url : String, callback : (data : MutableList<String>?) -> Unit) {
    thread {
        val json : String? = getUrl(url)
        val responseObj : ResponseObject = ObjectMapper().readValue(json, ResponseObject::class.java)
        val responseList : ResponseList? = responseObj.response
        val countries : MutableList<Country>? = responseList?.countries
        var countryNames = mutableListOf<String>()
        countries?.forEach { countryNames.add(it.country_name.toString()) }
        callback(countryNames)
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
