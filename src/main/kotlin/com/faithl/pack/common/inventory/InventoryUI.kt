package com.faithl.pack.common.inventory

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

/**
 * @author Leosouthey
 * @time 2021/12/3-17:56
 **/
abstract class InventoryUI {
    abstract fun build(player: Player, page: Int): Inventory
    abstract fun open(player:Player,page:Int)

    companion object{
        val inventoryViewing = mutableMapOf<Player,Inventory?>()
        val packViewing = mutableMapOf<Player,Pack?>()
        val packPageViewing = mutableMapOf<Player,Int?>()
    }
}