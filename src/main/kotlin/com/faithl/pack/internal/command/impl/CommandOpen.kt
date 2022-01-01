package com.faithl.pack.internal.command.impl

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.common.inventory.Pack
import com.faithl.pack.common.inventory.PackUI
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.onlinePlayers
import taboolib.library.xseries.XSound
import taboolib.module.lang.sendLang
import taboolib.platform.util.sendLang

object CommandOpen {

    val command = subCommand {
        dynamic(commit = "pack") {
            suggestion<ProxyPlayer> { _, _ ->
                Pack.packList.map {
                    it.name.toString()
                }
            }
            execute<ProxyPlayer> { sender, _, argument ->
                val pack = FaithlPackAPI.getPack(argument)
                pack?.ui?.open(sender.cast(), 1) ?: return@execute
                sender.sendLang("Player-Opened-Pack", pack.name!!)
                val senderPlayer: Player = sender.cast()
                XSound.BLOCK_ENDER_CHEST_OPEN.play(senderPlayer)
            }
            dynamic(commit = "player", permission = "faithlpack.open.other") {
                suggestion<ProxyCommandSender> { _, _ ->
                    onlinePlayers().map {
                        it.name
                    }
                }
                execute<ProxyCommandSender> { sender, context, argument ->
                    val pack = FaithlPackAPI.getPack(context.argument(-1))
                    val player = Bukkit.getPlayerExact(argument)
                    pack?.ui?.open(player!!, 1) ?: return@execute
                    player!!.sendLang("Player-Opened-Pack", pack.name!!)
                    sender.sendLang("Command-Open-Info", player.name, pack.name)
                    XSound.BLOCK_ENDER_CHEST_OPEN.play(player)
                    val senderPlayer: Player = sender.cast()
                    XSound.BLOCK_ENDER_CHEST_OPEN.play(senderPlayer)
                }
            }
        }
    }

}