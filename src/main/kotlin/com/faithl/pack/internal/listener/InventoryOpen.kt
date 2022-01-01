package com.faithl.pack.internal.listener

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.common.inventory.InventoryUI
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.getItemTag

/**
 * @author Leosouthey
 * @time 2021/12/10-19:54
 **/
object InventoryOpen {

    @SubscribeEvent
    fun e(e: InventoryOpenEvent) {
        if (e.player !is Player) {
            return
        }
        val player = e.player as Player
        if (InventoryUI.inventoryViewing[player] != null) {
            return
        }
        for (item in e.inventory.storageContents) {
            if (item == null) {
                continue
            }
            val type = item.getItemTag().getDeep("pack.type") ?: continue
            if (type.asString() == "bind") {
                e.isCancelled = true
                FaithlPackAPI.getPack(item.getItemTag().getDeep("pack.bind").asString())?.ui?.open(player, 1)
                break
            }
        }
    }

}