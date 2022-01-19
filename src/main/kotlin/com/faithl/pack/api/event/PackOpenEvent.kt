package com.faithl.pack.api.event

import com.faithl.pack.common.inventory.Pack
import org.bukkit.entity.Player
import taboolib.common.platform.event.ProxyEvent
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author Leosouthey
 * @since 2021/12/10-17:05
 **/
class PackOpenEvent(val player: Player, val pack: Pack, val page: Int) : BukkitProxyEvent() {

}