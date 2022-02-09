package com.faithl.pack.internal.listener

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.common.inventory.InventoryUI
import com.faithl.pack.internal.command.impl.CommandUnbind
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.adaptPlayer
import taboolib.module.nms.getItemTag
import taboolib.platform.util.isAir

/**
 * @author Leosouthey
 * @since 2021/12/10-19:54
 **/
object InventoryOpen {

    @SubscribeEvent
    fun e(e: InventoryOpenEvent) {
        if (e.player !is Player) {
            return
        }
        val player = e.player as Player
        if (InventoryUI.openingInventory[player] != null) {
            return
        }
        if (e.inventory.type == InventoryType.WORKBENCH) {
            return
        }
        if (CommandUnbind.unbindPlayers.find { e.player == it } != null) {
            CommandUnbind.unbindPlayers.remove(e.player)
            return
        }
        for (item in e.inventory.storageContents) {
            if (item.isAir()) {
                continue
            }
            val type = item?.getItemTag()?.getDeep("pack.type") ?: continue
            if (type.asString() == "bind") {
                e.isCancelled = true
                FaithlPackAPI.getPack(item.getItemTag().getDeep("pack.bind").asString())?.ui?.open(player, player, 1)
                break
            }
        }
    }

}