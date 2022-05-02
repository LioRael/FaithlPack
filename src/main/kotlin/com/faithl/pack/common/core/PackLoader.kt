package com.faithl.pack.common.core

import com.faithl.pack.FaithlPack
import com.faithl.pack.internal.util.sendLangIfEnabled
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.function.console
import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.configuration.Configuration
import java.io.File

/**
 * @author Leosouthey
 * @since 2022/4/30-21:39
 **/
object PackLoader {

    private val folder by lazy {
        val folder = File(FaithlPack.plugin.dataFolder, "packs")
        if (!folder.exists()) {
            listOf(
                "example.yml"
            ).forEach { releaseResourceFile("packs/$it", true) }
        }
        folder
    }

    fun loadInventories(sender: ProxyCommandSender = console()) {
        val files = mutableListOf<File>().also { list ->
            list.addAll(filterMenuFiles(folder))
            list.addAll(FaithlPack.setting.getStringList("loader.packs-files").flatMap { filterMenuFiles(File(it)) })
        }
        val tasks = mutableListOf<File>().also { tasks ->
            files.forEach { file ->
                if (!tasks.any { it.nameWithoutExtension == file.nameWithoutExtension } && file.extension == "yml")
                    tasks.add(file)
            }
        }
        val serializingTime = System.currentTimeMillis()
        tasks.forEach {
            val conf = Configuration.loadFromFile(it)
            PackSetting(conf.getString("name")!!, conf)
        }
        sender.sendLangIfEnabled(
            "loader-loaded",
            PackSetting.instances.size,
            System.currentTimeMillis() - serializingTime
        )
    }

    private fun filterMenuFiles(file: File): List<File> {
        return mutableListOf<File>().run {
            if (file.isDirectory) {
                file.listFiles()?.forEach {
                    addAll(filterMenuFiles(it))
                }
            } else if (!file.name.startsWith("#")) {
                add(file)
            }
            this
        }
    }
}