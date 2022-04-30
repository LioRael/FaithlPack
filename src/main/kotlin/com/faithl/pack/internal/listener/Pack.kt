package com.faithl.pack.internal.listener

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.api.event.PackCloseEvent
import com.faithl.pack.api.event.PackSaveEvent
import com.faithl.pack.common.core.PackPlayer
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit

/**
 * @author Leosouthey
 * @since 2022/4/30-19:48
 **/
object Pack {

    @SubscribeEvent
    fun e(e: PlayerQuitEvent) {
        val instance = PackPlayer.match(e.player)
        PackPlayer.instances.remove(instance)
    }

    @SubscribeEvent
    fun e(e: PackCloseEvent) {
        val event = PackSaveEvent(e.player, e.packData)
        if (event.call()) {
            event.packData.setPageItems(e.page, e.inventory)
            FaithlPackAPI.save(event.player, event.packData)
        }
    }

    @Awake(LifeCycle.ENABLE)
    fun autoSave() {
        submit(async = true, period = 20 * 60) {
            FaithlPackAPI.openingPacks.forEach {
                it.packData.setPageItems(it.page, it.inventory)
                FaithlPackAPI.save(it.player, it.packData)
            }
        }
    }

}