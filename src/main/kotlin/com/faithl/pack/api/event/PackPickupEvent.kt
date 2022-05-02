package com.faithl.pack.api.event

import com.faithl.pack.common.core.PackData
import org.bukkit.entity.Player
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author Leosouthey
 * @since 2022/5/2-17:20
 **/
class PackPickupEvent(val player: Player, val packData: PackData, val page: Int) : BukkitProxyEvent()