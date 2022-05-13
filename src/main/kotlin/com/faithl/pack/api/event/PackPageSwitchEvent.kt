package com.faithl.pack.api.event

import com.faithl.pack.common.core.PackData
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author Leosouthey
 * @since 2022/4/30-20:07
 **/
class PackPageSwitchEvent(
    val player: Player,
    val opener: Player,
    val previousInventory: Inventory,
    var packData: PackData,
    val previousPage: Int,
    var targetPage: Int
) : BukkitProxyEvent() {
    override val allowCancelled: Boolean
        get() = true
}