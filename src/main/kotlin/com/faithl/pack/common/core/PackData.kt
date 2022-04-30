package com.faithl.pack.common.core

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.api.event.PackOpenEvent
import com.faithl.pack.common.item.ItemBuilder
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.getItemTag
import taboolib.platform.util.sendLang
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Leosouthey
 * @since 2022/4/30-17:09
 **/
class PackData(val name: String, val data: ConcurrentHashMap<Int, ItemStack>) {

    fun open(player: Player, page: Int) {
        val permission = getSetting().permission
        if (permission != null && !player.hasPermission(permission)) {
            player.sendLang("player-no-permission")
            return
        }
        val rows = getSetting().rows
        if (rows > 6) {
            throw Exception("The number of rows exceeded the limit.(it has to be less than 6)")
        }
        val inventory = ItemBuilder.buildInventory(player, this, page, rows)
        FaithlPackAPI.openingPacks.add(OpeningPack(player, this, inventory))
        val event = PackOpenEvent(player, this, page, inventory)
        if (event.call()) {
            player.openInventory(event.inventory)
        }
    }

    fun getSetting(): PackSetting {
        return PackSetting.instances.find { it.name == name }!!
    }

    fun getPageItems(page: Int): ConcurrentHashMap<Int, ItemStack> {
        val rows = getSetting().rows
        if (rows > 6) {
            throw Exception("The number of rows exceeded the limit.(it has to be less than 6)")
        } else {
            val items = ConcurrentHashMap<Int, ItemStack>()
            var index = 0
            for (i in (page - 1) * (rows - 1) * 9 until page * (rows - 1) * 9 - 1) {
                val item = data[i] ?: continue
                items[index] = item
                index += 1
            }
            return items
        }
    }

    fun setPageItems(page: Int, inventory: Inventory) {
        val rows = getSetting().rows
        var index = 0
        for (i in (page - 1) * (rows - 1) * 9 until page * (rows - 1) * 9 - 1) {
            val item = inventory.getItem(i) ?: continue
            val tag = item.getItemTag()
            if (tag.getDeep("pack.type") != null) {
                continue
            }
            data[i] = item
            index += 1
        }
    }

}