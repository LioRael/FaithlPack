package com.faithl.pack.common.inventory

import com.faithl.pack.api.FaithlPackAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import java.util.UUID

/**
 * @author Leosouthey
 * @since 2021/12/3-17:56
 **/
abstract class InventoryUI {
    abstract fun build(player: Player, page: Int): Inventory
    abstract fun open(target: Player, player: Player, page: Int)

    companion object {

        val openingInventory = mutableMapOf<UUID, Inventory?>()
        val openingPack = mutableMapOf<UUID, Pack?>()
        val openingPage = mutableMapOf<UUID, Int?>()
        val openingOwner = mutableMapOf<UUID, UUID?>()

        fun saveInventory(sender: UUID) {
            val player = Bukkit.getPlayer(sender) ?: return
            val inv = openingInventory[sender] ?: return
            val pack = openingPack[sender] ?: return
            val page = openingPage[sender] ?: return
            FaithlPackAPI.setPack(player, pack, page, inv)
        }

        fun saveInventory(player: Player) {
            saveInventory(player.uniqueId)
        }
    }

}