package com.faithl.pack.api.event

import com.faithl.pack.common.core.PackData
import org.bukkit.entity.Player
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author Leosouthey
 * @since 2022/4/30-20:07
 **/
class PackPageSwitchEvent(val player: Player, var packData: PackData, var page: Int) : BukkitProxyEvent()