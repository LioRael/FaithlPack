package com.faithl.pack.api.event

import com.faithl.pack.common.core.PackData
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author Leosouthey
 * @since 2022/4/30-19:55
 **/
class PackOpenEvent(val player: Player, val packData: PackData, val page: Int, var inventory: Inventory) :
    BukkitProxyEvent()