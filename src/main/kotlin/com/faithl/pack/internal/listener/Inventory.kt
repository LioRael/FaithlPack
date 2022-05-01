package com.faithl.pack.internal.listener

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.api.event.PackClickEvent
import com.faithl.pack.api.event.PackCloseEvent
import com.faithl.pack.api.event.PackPlaceItemEvent
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author Leosouthey
 * @since 2022/4/30-19:59
 **/
object Inventory {

    @SubscribeEvent
    fun e(e: InventoryCloseEvent) {
        FaithlPackAPI.openingPacks.find {
            it.player == e.player
        }?.let {
            PackCloseEvent(e.player as Player, it.packData, it.page, e.inventory).call()
            FaithlPackAPI.openingPacks.remove(it)
        }
    }

    @SubscribeEvent
    fun ce1(e: InventoryClickEvent) {
        FaithlPackAPI.openingPacks.find {
            it.player == e.whoClicked
        }?.let {
            val event = PackClickEvent(
                it.player,
                it.packData,
                it.page,
                it.inventory,
                e.currentItem,
                e.isLeftClick,
                e.isRightClick
            ).call()
            if (!event) {
                e.isCancelled = true
            }
        }
    }

    @SubscribeEvent
    fun ce2(e: InventoryClickEvent) {
        FaithlPackAPI.openingPacks.find {
            it.player == e.whoClicked
        }?.let {
            val player = it.player
            if (e.action == InventoryAction.DROP_ALL_CURSOR || e.action == InventoryAction.DROP_ALL_SLOT || e.action == InventoryAction.DROP_ONE_CURSOR || e.action == InventoryAction.DROP_ONE_SLOT) {
                e.isCancelled = true
                return
            }
            val event = PackPlaceItemEvent(player, it.inventory, it.packData, it.page, e.action, e.currentItem).call()
            if (!event) {
                e.isCancelled = true
            }
        }
    }

}