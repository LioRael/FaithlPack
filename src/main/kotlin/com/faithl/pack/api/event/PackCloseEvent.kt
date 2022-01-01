package com.faithl.pack.api.event

import com.faithl.pack.common.inventory.Pack
import org.bukkit.entity.Player
import taboolib.common.platform.event.ProxyEvent

/**
 * @author Leosouthey
 * @time 2022/1/1-18:22
 **/
class PackCloseEvent(player: Player, pack: Pack, page: Int) : ProxyEvent()