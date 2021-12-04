package com.faithl.pack.common.util

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection

/**
 * Json util
 * @author Leosouthey
 * @constructor Create empty Json util
 */
object JsonUtil {
    fun loadJson(url: String?): String{
        val json = StringBuilder()
        try {
            val urlObject = URL(url)
            val uc: URLConnection = urlObject.openConnection()
            val `in` = BufferedReader(InputStreamReader(uc.getInputStream()))
            var inputLine:String?
            while (`in`.readLine().also {inputLine = it } != null) {
                json.append(inputLine)
            }
            `in`.close()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json.toString()
    }
}