package com.faithl.pack.internal.listener

import com.faithl.pack.common.inventory.InventoryUI
import com.faithl.pack.common.util.condition
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.isAir
import taboolib.platform.util.sendLang

/**
 * @author Leosouthey
 **/
object InventoryPlace {

    @SubscribeEvent
    fun e(e: InventoryClickEvent) {
        if (e.whoClicked !is Player) {
            return
        }
        val player = e.whoClicked as Player
        if (e.clickedInventory != player.inventory) {
            return
        }
        if (InventoryUI.inventoryViewing[player] == null) {
            return
        }
        if (InventoryUI.inventoryViewing[player] != e.inventory) {
            return
        }
        val pack = InventoryUI.packViewing[player]
        val itemStack = player.inventory.getItem(e.slot) ?: return
        if (itemStack.isAir()) {
            return
        }
        if (pack?.sort?.getBoolean("must-condition") == true) {
            if (e.action == InventoryAction.PICKUP_ALL
                || e.action == InventoryAction.PICKUP_HALF
                || e.action == InventoryAction.PLACE_ONE
                || e.action == InventoryAction.PICKUP_SOME
                || e.action == InventoryAction.MOVE_TO_OTHER_INVENTORY
                || e.action == InventoryAction.SWAP_WITH_CURSOR
            ) {
                if (!condition(pack, itemStack)) {
                    player.sendLang("Pack-Put-Error", pack.name!!)
                    e.isCancelled = true
                }
            }
        }
    }

}