package com.faithl.pack.internal.command.impl

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.common.core.PackSetting
import com.faithl.pack.internal.database.Database
import com.faithl.pack.internal.util.sendLangIfEnabled
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.onlinePlayers
import taboolib.library.xseries.XSound

object CommandOpen {

    val command = subCommand {
        dynamic(commit = "pack") {
            suggestion<ProxyCommandSender> { _, _ ->
                PackSetting.instances.map {
                    it.name
                }
            }
            execute<ProxyPlayer> { sender, _, argument ->
                val packData = Database.INSTANCE.getPackData(sender.uniqueId, argument)
                FaithlPackAPI.open(sender.cast(), packData, 1)
                sender.sendLangIfEnabled("player-opened-pack", packData.name)
                val senderPlayer: Player = sender.cast() ?: return@execute
                XSound.BLOCK_ENDER_CHEST_OPEN.play(senderPlayer)
            }
            dynamic(commit = "player", permission = "faithlpack.open.other") {
                suggestion<ProxyCommandSender> { _, _ ->
                    onlinePlayers().map {
                        it.name
                    }
                }
                execute<ProxyCommandSender> { sender, context, argument ->
                    val player = Bukkit.getPlayerExact(argument) ?: return@execute
                    val packData = Database.INSTANCE.getPackData(player.uniqueId, context.argument(-1))
                    FaithlPackAPI.open(player, packData, 1)
                    player.sendLangIfEnabled("player-opened-pack", packData.name)
                    sender.sendLangIfEnabled("command-open-info", player.name, packData.name)
                    XSound.BLOCK_ENDER_CHEST_OPEN.play(player)
                    val senderPlayer: Player = sender.cast() ?: return@execute
                    XSound.BLOCK_ENDER_CHEST_OPEN.play(senderPlayer)
                }
            }
        }
    }

}