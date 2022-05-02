package com.faithl.pack

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.common.core.PackLoader
import com.faithl.pack.internal.util.sendLangIfEnabled
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.common.platform.function.pluginVersion
import taboolib.common.platform.function.runningPlatform
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.metrics.Metrics
import taboolib.platform.BukkitPlugin

/**
 * @author Leosouthey
 * @since 2022/4/30-16:36
 **/
object FaithlPack : Plugin() {

    @Config("settings.yml", migrate = true, autoReload = true)
    lateinit var setting: Configuration
        private set

    val plugin by lazy { BukkitPlugin.getInstance() }

    override fun onLoad() {
        Metrics(13490, pluginVersion, runningPlatform)
    }

    override fun onEnable() {
        PackLoader.loadInventories()
        console().sendLangIfEnabled("plugin-enabled", pluginVersion, KotlinVersion.CURRENT.toString())
    }

    override fun onDisable() {
        FaithlPackAPI.openingPacks.forEach {
            FaithlPackAPI.save(it.player, it.packData)
        }
        console().sendLangIfEnabled("plugin-disabled")
    }

}