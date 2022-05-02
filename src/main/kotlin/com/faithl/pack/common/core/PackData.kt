package com.faithl.pack.common.core

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.api.event.PackOpenEvent
import com.faithl.pack.common.item.ItemBuilder
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.getItemTag
import taboolib.platform.util.sendLang

/**
 * @author Leosouthey
 * @since 2022/4/30-17:09
 **/
class PackData(val name: String, val data: MutableMap<Int, ItemStack?> = mutableMapOf()) {

    fun open(player: Player, page: Int) {
        if (page <= 0) {
            player.sendLang("pack-page-switch-limit")
            return
        }
        if (page > getSetting().inventory!!.getInt("pages")) {
            player.sendLang("pack-page-switch-limit")
            return
        }
        val permission = getSetting().permission
        if (permission != null && !player.hasPermission(permission)) {
            player.sendLang("player-no-permission")
            return
        }
        val inventory = build(player, page)
        FaithlPackAPI.openingPacks.add(OpeningPack(player, this, inventory, page))
        val event = PackOpenEvent(player, this, page, inventory)
        if (event.call()) {
            player.openInventory(event.inventory)
        }
    }

    fun build(player: Player, page: Int): Inventory {
        val rows = getSetting().rows
        if (rows > 6) {
            throw Exception("The number of rows exceeded the limit.(it has to be less than 6)")
        }
        return ItemBuilder.buildInventory(player, this, page, rows)
    }

    fun getSetting(): PackSetting {
        return PackSetting.instances.find { it.name == name }!!
    }

    fun getPageItems(page: Int): MutableMap<Int, ItemStack> {
        val rows = getSetting().rows
        if (rows > 6) {
            throw Exception("The number of rows exceeded the limit.(it has to be less than 6)")
        } else {
            val items = mutableMapOf<Int, ItemStack>()
            for (i in (page - 1) * (rows - 1) * 9 until page * (rows - 1) * 9 - 1) {
                val item = data[i] ?: continue
                items[i % ((rows - 1) * 9)] = item
            }
            return items
        }
    }

    fun setPageItems(page: Int, inventory: Inventory) {
        var index = 0
        for (i in (page - 1) * (inventory.size - 9) until page * (inventory.size - 9)) {
            val item = inventory.getItem(index)
            val tag = item?.getItemTag()
            if (tag != null) {
                if (tag.getDeep("pack.type") != null) {
                    continue
                }
            }
            if (item == null) {
                data.remove(i)
            } else {
                data[i] = item
            }
            index += 1
        }
    }

}