package com.faithl.pack.internal.command.impl

import com.faithl.pack.internal.database.Database
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.module.lang.sendLang

object CommandSave {

    val command = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            Database.cache.forEach { (key, value) ->
                value.data.forEach {
                    Database.INSTANCE.setPackData(key, it)
                }
            }
            sender.sendLang("command-save-info")
        }
    }

}