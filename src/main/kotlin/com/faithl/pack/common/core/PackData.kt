package com.faithl.pack.common.core

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.api.event.PackOpenEvent
import com.faithl.pack.common.item.ItemBuilder
import com.faithl.pack.internal.util.sendLangIfEnabled
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.getItemTag

/**
 * @author Leosouthey
 * @since 2022/4/30-17:09
 **/
class PackData(val name: String, val data: MutableMap<Int, ItemStack?> = mutableMapOf()) {

    /**
     * 打开玩家仓库
     *
     * @param player 玩家
     * @param page 页数
     * @param opener 打开者
     */
    fun open(player: Player, page: Int, opener: Player = player) {
        if (page <= 0) {
            opener.sendLangIfEnabled("pack-page-switch-limit")
            return
        }
        if (page > getSetting().inventory!!.getInt("pages")) {
            opener.sendLangIfEnabled("pack-page-switch-limit")
            return
        }
        val permission = getSetting().permission
        if (permission != null && !opener.hasPermission(permission)) {
            opener.sendLangIfEnabled("player-no-permission")
            return
        }
        FaithlPackAPI.preopeningPack.add(PreopeningPack(opener, this, page))
        val inventory = build(player, page)
        FaithlPackAPI.preopeningPack.removeIf { it.player === opener }
        FaithlPackAPI.openingPacks.add(OpeningPack(opener, this, inventory, page))
        val event = PackOpenEvent(opener, this, page, inventory)
        if (event.call()) {
            opener.openInventory(event.inventory)
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
            for (i in (page - 1) * (rows - 1) * 9 until page * (rows - 1) * 9) {
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