package com.faithl.pack.internal.command.impl

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.common.inventory.Pack
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
                Pack.packList.map {
                    it.name.toString()
                }
            }
            execute<ProxyPlayer> { sender, _, argument ->
                val pack = FaithlPackAPI.getPack(argument) ?: return@execute
                val bindItem = buildBindItem(pack)
                sender.sendLang("Command-Bind-Info", sender.name,pack.name!!)
                val senderPlayer: Player = sender.cast()
                XSound.ENTITY_ITEM_PICKUP.play(senderPlayer)
                senderPlayer.giveItem(bindItem)
            }
            dynamic(commit = "player",permission = "faithlpack.bind.other") {
                suggestion<ProxyCommandSender> { _, _ ->
                    onlinePlayers().map {
                        it.name
                    }
                }
                execute<ProxyPlayer> { sender, context, argument ->
                    val player = Bukkit.getPlayerExact(argument) ?: return@execute
                    val pack = FaithlPackAPI.getPack(context.argument(-1)) ?: return@execute
                    val bindItem = buildBindItem(pack)
                    sender.sendLang("Command-Bind-Info",player.name, pack.name!!)
                    player.sendLang("Player-Bind", pack.name)
                    XSound.ENTITY_ITEM_PICKUP.play(player)
                    player.giveItem(bindItem)
                }
            }
        }
    }

    fun buildBindItem(pack:Pack):ItemStack{
        return buildItem(pack.sort?.getString("bind.material")?.parseToXMaterial()!!){
            name = pack.sort.getString("bind.name")!!.colored()
            lore += pack.sort.getStringList("bind.lore").colored()
            if (pack.sort.getBoolean("bind.shiny")){
                shiny()
            }
        }.apply {
            getItemTag().also { itemTag ->
                itemTag.putDeep("pack.type","bind")
                itemTag.putDeep("pack.bind",pack.name)
                itemTag.saveTo(this)
            }
        }
    }

}