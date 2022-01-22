package com.faithl.pack.internal.command.impl

import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.sendLang

object CommandUnbind {

    val unbindPlayers = mutableListOf<Player>()

    val command = subCommand {
        execute<Player> { sender, _, _ ->
            if (unbindPlayers.find { sender == it} != null){
                unbindPlayers.remove(sender)
                sender.sendLang("Command-Unbind-Quit")
                return@execute
            }
            unbindPlayers.add(sender)
            sender.sendLang("Command-Unbind-Info")
        }
    }


}