package com.faithl.pack.api.event

import com.faithl.pack.common.core.PackData
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author Leosouthey
 * @since 2022/5/1-17:11
 **/
class PackPlaceItemEvent(
    val player: Player,
    val opener: Player,
    val inventory: Inventory,
    var packData: PackData,
    val page: Int,
    val action: InventoryAction,
    val currentItem: ItemStack?,
) : BukkitProxyEvent() {
    override val allowCancelled: Boolean
        get() = true
}