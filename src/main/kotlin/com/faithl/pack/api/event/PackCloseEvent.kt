package com.faithl.pack.api.event

import com.faithl.pack.common.core.PackData
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author Leosouthey
 * @since 2022/4/30-19:56
 **/
class PackCloseEvent(
    val player: Player,
    val opener: Player,
    val packData: PackData,
    val page: Int,
    var inventory: Inventory
) :
    BukkitProxyEvent()