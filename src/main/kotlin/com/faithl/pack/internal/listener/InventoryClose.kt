package com.faithl.pack.internal.listener

import com.faithl.pack.api.event.PackCloseEvent
import com.faithl.pack.common.inventory.InventoryUI
import org.bukkit.Bukkit
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
        if (InventoryUI.openingInventory[player.uniqueId] == null) {
            return
        }
        if (InventoryUI.openingInventory[player.uniqueId] != e.inventory) {
            return
        }
        val owner = InventoryUI.openingOwner[player.uniqueId]?.let { Bukkit.getPlayer(it) } ?: return
        val event = PackCloseEvent(
            player,
            owner, InventoryUI.openingPack[player.uniqueId]!!, InventoryUI.openingPage[player.uniqueId]!!,
            InventoryUI.openingInventory[player.uniqueId]!!
        )
        event.call()
        InventoryUI.openingInventory[player.uniqueId] = null
        InventoryUI.openingPack[player.uniqueId] = null
        InventoryUI.openingPage[player.uniqueId] = null
        InventoryUI.openingOwner[player.uniqueId] = null
    }

}