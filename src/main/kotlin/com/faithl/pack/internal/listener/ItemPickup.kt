package com.faithl.pack.internal.listener

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.api.event.PackPickupEvent
import com.faithl.pack.common.inventory.InventoryUI
import com.faithl.pack.common.inventory.Pack
import com.faithl.pack.common.inventory.PackUI
import com.faithl.pack.common.util.condition
import com.faithl.pack.common.util.putItem
import com.faithl.pack.internal.data.Database
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.library.xseries.XSound
import taboolib.module.nms.getName
import taboolib.platform.util.sendLang

object ItemPickup {

    @SubscribeEvent
    fun e(e: EntityPickupItemEvent) {
        if (e.entity !is Player) {
            return
        }
        val player = e.entity as Player
        pack@ for (pack in Pack.packList) {
            if (pack.sort?.getBoolean("auto-pickup.enabled") ?: continue) {
                if (!Database.INSTANCE.getAutoPickup(player, pack)) {
                    continue
                }
            }
            if (pack.permission != null && !player.hasPermission(pack.permission)) {
                continue
            }
            val autoPickupPermission = pack.sort.getString("auto-pickup.permission")
            if (autoPickupPermission != null && !player.hasPermission(autoPickupPermission)) {
                continue
            }
            if (condition(pack, e.item.itemStack)) {
                val itemStack = e.item.itemStack.clone()
                page@ for (page in 1..pack.inventoryConfig!!.getInt("pages")) {
                    println(itemStack.amount)
                    PackPickupEvent(player, pack, page).call()
                    if (pack.ui is PackUI) {
                        if (InventoryUI.openingInventory[player] != null) {
                            player.closeInventory()
                        }
                        val newPack = (pack.ui as PackUI).getData(player, page)
                        newPack!!.putItem(itemStack)
                        FaithlPackAPI.setPack(player, pack, page, newPack)
                        if (itemStack.amount == 0) {
                            break@page
                        }
                        if (itemStack.amount > 0) {
                            continue@page
                        }
                    }
                }
                if (e.item.itemStack.amount - itemStack.amount != 0) {
                    player.sendLang(
                        "Pack-Auto-Pick-Info",
                        e.item.itemStack.amount - itemStack.amount,
                        e.item.itemStack.getName(),
                        pack.name!!
                    )
                }
                if (itemStack.amount == 0) {
                    e.isCancelled = true
                    e.item.remove()
                } else {
                    e.item.itemStack = itemStack
                }
                XSound.ENTITY_ITEM_PICKUP.play(player)
                break@pack
            }
        }
    }
}