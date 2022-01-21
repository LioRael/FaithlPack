package com.faithl.pack.internal.command

import com.faithl.pack.FaithlPack
import com.faithl.pack.internal.command.impl.CommandBind
import com.faithl.pack.internal.command.impl.CommandOpen
import com.faithl.pack.internal.command.impl.CommandReload
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.*
import taboolib.common.platform.function.adaptCommandSender
import taboolib.module.chat.TellrawJson
import taboolib.platform.util.asLangText

/**
 * Command handler
 * @author Leosouthey
 * @constructor Create empty Command handler
 */
@CommandHeader(name = "faithlpack", aliases = ["fpack"], permissionDefault = PermissionDefault.TRUE)
object CommandHandler {

    @CommandBody(permission = "faithlpack.reload")
    val reload = CommandReload.command

    @CommandBody(permission = "faithlpack.open")
    val open = CommandOpen.command

    @CommandBody(permission = "faithlpack.bind")
    val bind = CommandBind.command

    @CommandBody(permission = "faithlpack.access")
    val main = mainCommand {
        execute<CommandSender> { sender, _, argument ->
            if (argument.isEmpty()) {
                generateMainHelper(sender)
                return@execute
            }
        }
    }

    @CommandBody(permission = "faithlpack.access")
    val help = subCommand {
        execute<CommandSender> { sender, _, _ ->
            generateMainHelper(sender)
        }
    }

    private fun generateMainHelper(sender: CommandSender) {
        val proxySender = adaptCommandSender(sender)
        proxySender.sendMessage("")
        TellrawJson()
            .append("  ").append("§bFaithlPack")
            .hoverText("§7FaithlPack is modern and advanced Minecraft pack-plugin")
            .append(" ").append("§f${FaithlPack.plugin.description.version}")
            .hoverText(
                """
                §7Plugin version: §b${FaithlPack.plugin.description.version}
            """.trimIndent()
            ).sendTo(proxySender)
        proxySender.sendMessage("")
        TellrawJson()
            .append("  §7${sender.asLangText("Command-Help-Type")}: ").append("§f/FaithlPack §8[...]")
            .hoverText("§f/FaithlPack §8[...]")
            .suggestCommand("/FaithlPack ")
            .sendTo(proxySender)
        proxySender.sendMessage("  §7${sender.asLangText("Command-Help-Args")}:")
        fun displayArg(name: String, desc: String) {
            TellrawJson()
                .append("    §8- ").append("§f$name")
                .hoverText("§f/FaithlPack $name §8- §7$desc")
                .suggestCommand("/FaithlPack $name ")
                .sendTo(proxySender)
            proxySender.sendMessage("      §7$desc")
        }
        displayArg("open", sender.asLangText("Command-Open-Description"))
        displayArg("bind", sender.asLangText("Command-Bind-Description"))
        displayArg("reload", sender.asLangText("Command-Reload-Description"))
        proxySender.sendMessage("")
    }

}