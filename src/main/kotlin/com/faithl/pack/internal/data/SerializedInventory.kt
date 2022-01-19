package com.faithl.pack.internal.data;

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.common.inventory.Pack
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.Inventory
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author Leosouthey
 * @since 2021/12/10-20:15
 **/
data class SerializedInventory(val player: Player) {

    val inventories = mutableMapOf<MutableMap<Pack, Int>, Inventory>()
    val autoPickup = mutableMapOf<Pack, Boolean>()

    init {
        instances[player] = this
    }

    companion object {

        val instances: MutableMap<Player, SerializedInventory> = mutableMapOf()

        fun getInstance(player: Player): SerializedInventory {
            return instances[player]!!
        }

        @SubscribeEvent
        fun e(e: PlayerQuitEvent) {
            instances[e.player]!!.inventories.forEach { (packData, inventory) ->
                packData.forEach { (pack, page) ->
                    FaithlPackAPI.setPack(e.player, pack, page, inventory)
                }
            }
            instances.remove(e.player)
        }

        @SubscribeEvent
        fun e(e: PlayerKickEvent) {
            instances[e.player]!!.inventories.forEach { (packData, inventory) ->
                packData.forEach { (pack, page) ->
                    FaithlPackAPI.setPack(e.player, pack, page, inventory)
                }
            }
            instances.remove(e.player)
        }

        @SubscribeEvent
        fun e(e: PlayerLoginEvent) {
            SerializedInventory(e.player)
            Pack.packList.forEach {
                Database.INSTANCE.getPack(e.player, it)
            }
        }
    }

}