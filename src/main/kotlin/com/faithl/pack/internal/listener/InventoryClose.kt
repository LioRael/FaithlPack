package com.faithl.pack.internal.listener

import com.faithl.pack.api.event.PackCloseEvent
import com.faithl.pack.common.inventory.InventoryUI
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import taboolib.common.platform.event.SubscribeEvent

object InventoryClose {

    @SubscribeEvent
    fun e(e: InventoryCloseEvent) {
        if (e.player !is Player) {
            return
        }
        val player = e.player as Player
        if (InventoryUI.openingInventory[player] == null) {
            return
        }
        if (InventoryUI.openingInventory[player] != e.inventory) {
            return
        }
        val event = PackCloseEvent(player,
            InventoryUI.openingOwner[player]!!, InventoryUI.openingPack[player]!!, InventoryUI.openingPage[player]!!,
            InventoryUI.openingInventory[player]!!
        )
        event.call()
        InventoryUI.openingInventory[player] = null
        InventoryUI.openingPack[player] = null
        InventoryUI.openingPage[player] = null
        InventoryUI.openingOwner[player] = null
    }

}