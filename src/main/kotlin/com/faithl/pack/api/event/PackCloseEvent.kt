package com.faithl.pack.api.event

import com.faithl.pack.common.inventory.Pack
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import taboolib.common.platform.event.ProxyEvent
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author Leosouthey
 * @since 2022/1/1-18:22
 **/
class PackCloseEvent(val player: Player, val pack: Pack, val page: Int, val inv: Inventory) : BukkitProxyEvent() {

}