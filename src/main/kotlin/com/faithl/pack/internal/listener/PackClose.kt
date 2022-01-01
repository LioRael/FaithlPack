package com.faithl.pack.internal.listener

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.api.event.PackCloseEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author Leosouthey
 * @time 2022/1/1-20:18
 **/
object PackClose {

    @SubscribeEvent
    fun e(e: PackCloseEvent) {
        FaithlPackAPI.setPack(e.player, e.pack, e.page, e.inv)
    }

}