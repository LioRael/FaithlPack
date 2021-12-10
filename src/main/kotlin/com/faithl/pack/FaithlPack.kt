package com.faithl.pack

import com.alibaba.fastjson.JSONObject
import com.faithl.pack.common.inventory.InventoryUI
import com.faithl.pack.common.util.JsonUtil
import com.faithl.pack.internal.conf.PackLoader
import org.bukkit.entity.Player
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.common.platform.function.pluginVersion
import taboolib.common.platform.function.runningPlatform
import taboolib.common.util.Version
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.lang.sendLang
import taboolib.module.metrics.Metrics
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.sendLang

/**
 * Faithl pack
 * @author Leosouthey
 * @constructor Create empty Faithl pack
 */
@RuntimeDependency(value = "com.alibaba:fastjson:1.2.78")
object FaithlPack: Plugin() {

    @Config("settings.yml", migrate = true,autoReload = true)
    lateinit var setting: Configuration

    val plugin by lazy { BukkitPlugin.getInstance() }
    var outOfDate = false

    override fun onLoad() {
        Metrics(13490, pluginVersion, runningPlatform)
    }

    override fun onEnable() {
        init()
        console().sendLang("Plugin-Enabled",pluginVersion,KotlinVersion.CURRENT.toString())
    }

    override fun onDisable() {
        InventoryUI.inventoryViewing.forEach {
            it.key.closeInventory()
        }
        console().sendLang("Plugin-Disabled")
    }

    fun init(){
        PackLoader.loadInventories()
        checkUpdate()
    }

    /**
     * Check update
     *
     * @param sender
     */
    fun checkUpdate(sender: Player? = null){
        if(!setting.getBoolean("Options.check-update"))
            return
        val json = JsonUtil.loadJson("https://api.faithl.com/version.php?plugin=FaithlPack")
        val `object` = JSONObject.parseObject(json)
        val version = Version(`object`.getString("version"))
        if (version > Version(pluginVersion)){
            outOfDate = true
            if (sender == null){
                console().sendLang("Plugin-Update",pluginVersion,version)
            } else {
                sender.sendLang("Plugin-Update",pluginVersion,version.source,"https://www.mcbbs.net/thread-1281714-1-1.html")
            }
        }
    }
}