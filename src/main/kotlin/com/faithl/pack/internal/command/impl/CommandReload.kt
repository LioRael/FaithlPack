package com.faithl.pack.internal.command.impl

import com.faithl.pack.FaithlPack
import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.common.core.PackLoader
import com.faithl.pack.common.core.PackSetting
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.module.lang.sendLang

object CommandReload {

    val command = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            PackSetting.instances.clear()
            FaithlPack.setting.reload()
            FaithlPackAPI.openingPacks.forEach {
                it.player.closeInventory()
            }
            PackLoader.loadInventories(sender)
            sender.sendLang("command-reload-info")
        }
    }

}