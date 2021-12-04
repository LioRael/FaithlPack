package com.faithl.pack.internal.listener

import com.faithl.pack.FaithlPack.checkUpdate
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent

object PlayerJoin {

    @SubscribeEvent
    fun e(e:PlayerJoinEvent){
        checkUpdate(e.player)
    }

}