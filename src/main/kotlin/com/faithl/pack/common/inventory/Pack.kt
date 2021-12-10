package com.faithl.pack.common.inventory

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.common.util.serializeToString
import com.faithl.pack.internal.data.Database
import org.bukkit.entity.Player
import taboolib.library.configuration.ConfigurationSection

class Pack(root: ConfigurationSection?) {
    init {
        packList.add(this)
    }

    var ui:InventoryUI = PackUI(this)
    val name = root!!.getString("Name")
    val inventoryConfig = root!!.getConfigurationSection("Inventory")
    val enabledLock = root?.getBoolean("Enabled-Lock") ?: true
    val permission = root?.getString("Permission")
    val sort = root?.getConfigurationSection("Sort")

    companion object{
        val packList = mutableListOf<Pack>()
    }

    fun save(player:Player) {
        FaithlPackAPI.setPack(player, InventoryUI.packViewing[player]!!, InventoryUI.packPageViewing[player]!!,
            InventoryUI.inventoryViewing[player]!!
        )
    }
}