package com.faithl.pack.internal.listener

import com.faithl.pack.api.event.PackCloseEvent
import com.faithl.pack.common.inventory.InventoryUI
import com.faithl.pack.common.inventory.PackUI
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import taboolib.common.platform.event.SubscribeEvent

object InventoryClose {

    @SubscribeEvent
    fun e(e: InventoryCloseEvent) {
        if (e.player !is Player) {
            return
        }
        val viewer = e.player as Player
        if (InventoryUI.inventoryViewing[viewer] == null) {
            return
        }
        if (InventoryUI.inventoryViewing[viewer] != e.inventory) {
            return
        }
        PackCloseEvent(viewer, InventoryUI.packViewing[viewer]!!, InventoryUI.packPageViewing[viewer]!!).call()
        InventoryUI.packViewing[viewer]!!.save(viewer)
        InventoryUI.inventoryViewing[viewer] = null
        InventoryUI.packViewing[viewer] = null
        InventoryUI.packPageViewing[viewer] = null
    }

}