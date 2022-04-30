package com.faithl.pack.internal.listener

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.api.event.PackCloseEvent
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author Leosouthey
 * @since 2022/4/30-19:59
 **/
object Inventory {

    @SubscribeEvent
    fun e(e: InventoryCloseEvent) {
        FaithlPackAPI.openingPacks.forEach {
            if (it.inventory == e.inventory) {
                PackCloseEvent(e.player as Player, it.packData, it.page, it.inventory).call()
                FaithlPackAPI.openingPacks.remove(it)
                return
            }
        }
    }

}