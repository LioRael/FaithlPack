package com.faithl.pack.api.event

import com.faithl.pack.common.core.PackData
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author Leosouthey
 * @since 2022/5/1-16:53
 **/
class PackClickEvent(
    val player: Player,
    val packData: PackData,
    val page: Int,
    var inventory: Inventory,
    val item: ItemStack?,
    val isLeftClick: Boolean,
    val isRightClick: Boolean
) :
    BukkitProxyEvent() {
    override val allowCancelled: Boolean
        get() = true
}