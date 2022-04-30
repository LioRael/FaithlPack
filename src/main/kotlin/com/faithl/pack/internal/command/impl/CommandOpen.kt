package com.faithl.pack.internal.command.impl

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.common.core.PackPlayer
import com.faithl.pack.common.core.PackSetting
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.onlinePlayers
import taboolib.library.xseries.XSound
import taboolib.module.lang.sendLang
import taboolib.platform.util.sendLang

object CommandOpen {

    val command = subCommand {
        dynamic(commit = "pack") {
            suggestion<ProxyCommandSender> { _, _ ->
                PackSetting.instances.map {
                    it.name
                }
            }
            execute<ProxyPlayer> { sender, _, argument ->
                val packPlayer = PackPlayer.match(sender.cast())
                val packData = packPlayer.data.find {
                    it.name == argument
                } ?: return@execute
                FaithlPackAPI.open(sender.cast(), packData, 1)
                sender.sendLang("player-opened-pack", packData.name)
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
                    val packPlayer = PackPlayer.match(player)
                    val packData = packPlayer.data.find {
                        it.name == context.argument(-1)
                    } ?: return@execute
                    FaithlPackAPI.open(player, packData, 1)
                    player.sendLang("player-opened-pack", packData.name)
                    sender.sendLang("command-open-info", player.name, packData.name)
                    XSound.BLOCK_ENDER_CHEST_OPEN.play(player)
                    val senderPlayer: Player = sender.cast() ?: return@execute
                    XSound.BLOCK_ENDER_CHEST_OPEN.play(senderPlayer)
                }
            }
        }
    }

}