package com.faithl.pack.common.inventory

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

/**
 * @author Leosouthey
 * @time 2021/12/3-17:56
 **/
abstract class InventoryUI {
    abstract fun build(player: Player, page: Int): Inventory
    abstract fun open(player: Player, page: Int)

    companion object {

        val openingInventory = mutableMapOf<Player, Inventory?>()
        val openingPack = mutableMapOf<Player, Pack?>()
        val openingPage = mutableMapOf<Player, Int?>()

    }

}