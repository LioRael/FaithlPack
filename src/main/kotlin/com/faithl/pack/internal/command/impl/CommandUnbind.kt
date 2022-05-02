package com.faithl.pack.internal.command.impl

import com.faithl.pack.internal.util.sendLangIfEnabled
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import java.util.*

object CommandUnbind {

    val unbindPlayers = mutableListOf<UUID>()

    val command = subCommand {
        execute<Player> { sender, _, _ ->
            if (unbindPlayers.find { sender.uniqueId == it } != null) {
                unbindPlayers.remove(sender.uniqueId)
                sender.sendLangIfEnabled("command-unbind-quit")
                return@execute
            }
            unbindPlayers.add(sender.uniqueId)
            sender.sendLangIfEnabled("command-unbind-info")
        }
    }


}