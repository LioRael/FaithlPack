package com.faithl.pack.internal.command.impl

import com.faithl.pack.common.core.PackSetting
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.onlinePlayers
import taboolib.library.xseries.XSound
import taboolib.library.xseries.parseToXMaterial
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.module.nms.getItemTag
import taboolib.platform.util.buildItem
import taboolib.platform.util.giveItem
import taboolib.platform.util.sendLang

object CommandBind {

    val command = subCommand {
        dynamic(commit = "pack") {
            suggestion<ProxyPlayer> { _, _ ->
                PackSetting.instances.map {
                    it.name
                }
            }
            execute<ProxyPlayer> { sender, _, argument ->
                val pack = PackSetting.instances.find { it.name == argument } ?: return@execute
                val bindItem = buildBindItem(pack)
                sender.sendLang("command-bind-info", sender.name, pack.name)
                val senderPlayer: Player = sender.cast()
                XSound.ENTITY_ITEM_PICKUP.play(senderPlayer)
                senderPlayer.giveItem(bindItem)
            }
            dynamic(commit = "player", permission = "faithlpack.bind.other") {
                suggestion<ProxyCommandSender> { _, _ ->
                    onlinePlayers().map {
                        it.name
                    }
                }
                execute<ProxyPlayer> { sender, context, argument ->
                    val player = Bukkit.getPlayerExact(argument) ?: return@execute
                    val pack = PackSetting.instances.find { it.name == context.argument(-1) } ?: return@execute
                    val bindItem = buildBindItem(pack)
                    sender.sendLang("command-bind-info", player.name, pack.name)
                    player.sendLang("player-bind", pack.name)
                    XSound.ENTITY_ITEM_PICKUP.play(player)
                    player.giveItem(bindItem)
                }
            }
        }
    }

    fun buildBindItem(pack: PackSetting): ItemStack {
        return buildItem(pack.bind?.getString("material")?.parseToXMaterial()!!) {
            name = pack.bind.getString("name")!!.colored()
            lore += pack.bind.getStringList("lore")
            if (pack.bind.getBoolean("shiny")) {
                shiny()
            }
        }.apply {
            getItemTag().also { itemTag ->
                itemTag.putDeep("pack.type", "bind")
                itemTag.putDeep("pack.bind", pack.name)
                itemTag.saveTo(this)
            }
        }
    }

}