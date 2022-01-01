package com.faithl.pack.api.event

import com.faithl.pack.common.inventory.Pack
import org.bukkit.entity.Player
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author Leosouthey
 * @time 2021/12/10-17:05
 **/
class PackPickupEvent(val player: Player, val pack: Pack, val page: Int) : BukkitProxyEvent() {

}