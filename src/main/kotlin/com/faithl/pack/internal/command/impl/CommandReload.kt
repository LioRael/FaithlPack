package com.faithl.pack.internal.command.impl

import com.faithl.pack.FaithlPack
import com.faithl.pack.common.inventory.Pack
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.module.lang.sendLang

object CommandReload {
    val command = subCommand {
        execute<ProxyPlayer> { sender, _, _ ->
            Pack.packList.clear()
            FaithlPack.init()
            FaithlPack.setting.reload()
            sender.sendLang("Command-Reload-Info")
        }
    }
}