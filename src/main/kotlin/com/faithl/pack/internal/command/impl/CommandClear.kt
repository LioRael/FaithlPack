package com.faithl.pack.internal.command.impl

import com.faithl.pack.common.core.PackSetting
import com.faithl.pack.internal.database.Database
import com.faithl.pack.internal.util.sendLangIfEnabled
import org.bukkit.Bukkit
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.onlinePlayers

object CommandClear {

    val command = subCommand {
        dynamic(commit = "pack") {
            suggestion<ProxyPlayer> { _, _ ->
                PackSetting.instances.map {
                    it.name
                }
            }
            execute<ProxyPlayer> { sender, _, argument ->
                val pack = PackSetting.instances.first { it.name == argument }
                Database.INSTANCE.getPackData(sender.uniqueId, pack.name).data.clear()
                sender.sendLangIfEnabled("command-clear-info", sender.name, pack.name)
            }
            dynamic(commit = "player", permission = "faithlpack.clear.other") {
                suggestion<ProxyCommandSender> { _, _ ->
                    onlinePlayers().map {
                        it.name
                    }
                }
                execute<ProxyCommandSender> { sender, context, argument ->
                    val pack = PackSetting.instances.first { it.name == context.argument(-1) }
                    val player = Bukkit.getPlayerExact(argument) ?: return@execute
                    Database.INSTANCE.getPackData(player.uniqueId, pack.name).data.clear()
                    sender.sendLangIfEnabled("command-clear-info", player.name, pack.name)
                }
            }
        }
    }

}