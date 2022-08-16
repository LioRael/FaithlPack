package com.faithl.pack.internal.util

import com.faithl.pack.FaithlPack
import org.bukkit.entity.Player
import taboolib.common.platform.function.console
import taboolib.common.platform.function.pluginVersion
import taboolib.common.util.Version
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection

/**
 * @author Leosouthey
 * @since 2022/5/2 21:38
 **/
fun checkUpdate(sender: Player? = null): Boolean {
    try {
        if (!FaithlPack.setting.getBoolean("options.check-update")) {
            return true
        }
        val id = 1
        val url = "https://shuna.faithl.com/api/resource/${id}/version"
        val version = Version(getResult(url).replace("\"", ""))
        if (version > Version(pluginVersion)) {
            if (sender != null) {
                sender.sendLangIfEnabled(
                    "plugin-update",
                    pluginVersion,
                    version.source,
                    "https://beta.mcbbs.net/resource/8gu5gwi3"
                )
            } else {
                console().sendLangIfEnabled(
                    "plugin-update",
                    pluginVersion,
                    version.source,
                    "https://beta.mcbbs.net/resource/8gu5gwi3"
                )
            }
            return false
        }
        return true
    } catch (e: MalformedURLException) {
        e.printStackTrace()
        return true
    } catch (e: IOException) {
        e.printStackTrace()
        return true
    }
}

fun getResult(url: String): String {
    val result = StringBuilder()
    try {
        val urlObject = URL(url)
        val uc: URLConnection = urlObject.openConnection()
        val `in` = BufferedReader(InputStreamReader(uc.getInputStream()))
        var inputLine: String?
        while (`in`.readLine().also { inputLine = it } != null) {
            result.append(inputLine)
        }
        `in`.close()
    } catch (e: MalformedURLException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return result.toString()
}