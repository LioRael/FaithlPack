package com.faithl.pack.api.event

import com.faithl.pack.common.inventory.Pack
import org.bukkit.entity.Player
import taboolib.common.platform.event.ProxyEvent

/**
 * @author Leosouthey
 * @time 2021/12/10-17:05
 **/
class PackOpenEvent(player:Player,pack: Pack,page: Int) : ProxyEvent()