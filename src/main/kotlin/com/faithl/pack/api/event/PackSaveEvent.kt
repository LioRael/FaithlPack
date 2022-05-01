package com.faithl.pack.api.event

import com.faithl.pack.common.core.PackData
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author Leosouthey
 * @since 2022/4/30-19:48
 **/
class PackSaveEvent(val player: Player, var packData: PackData, val page: Int, val inventory: Inventory) :
    BukkitProxyEvent()