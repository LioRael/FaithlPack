package com.faithl.pack.internal.command

import com.faithl.pack.FaithlPack
import com.faithl.pack.internal.command.impl.CommandOpen
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.PermissionDefault
import taboolib.common.platform.command.mainCommand
import taboolib.module.chat.TellrawJson
import taboolib.module.lang.asLangText
import taboolib.module.nms.MinecraftVersion

@CommandHeader(name = "faithlpack", aliases = ["fp", "fpack"], permissionDefault = PermissionDefault.TRUE)
object CommandHandler {

    @AppearHelper
    @CommandBody(permission = "faithlpack.open")
    val open = CommandOpen.command

    @CommandBody
    val main = mainCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            generateMainHelper(sender)
        }
    }

    fun generateMainHelper(sender: ProxyCommandSender) {
        sender.sendMessage("")
        TellrawJson()
            .append("  ").append("§bFaithlPack")
            .hoverText("§7FaithlPack is modern and advanced Minecraft pack-plugin")
            .append(" ").append("§f${FaithlPack.plugin.description.version}")
            .hoverText(
                """
                §7Plugin version: §2${FaithlPack.plugin.description.version}
                §7NMS version: §b${MinecraftVersion.minecraftVersion}
            """.trimIndent()
            ).sendTo(sender)
        sender.sendMessage("")
        TellrawJson()
            .append("  §7${sender.asLangText("command-help-type")}: ").append("§f/FaithlPack §8[...]")
            .hoverText("§f/FaithlPack §8[...]")
            .suggestCommand("/FaithlPack ")
            .sendTo(sender)
        sender.sendMessage("  §7${sender.asLangText("command-help-args")}:")

        javaClass.declaredFields.forEach {
            if (!it.isAnnotationPresent(AppearHelper::class.java)) return@forEach
            val name = it.name
            val desc = sender.asLangText("command-$name-description")
            TellrawJson()
                .append("    §8- ").append("§f$name")
                .hoverText("§f/FaithlPack $name §8- §7$desc")
                .suggestCommand("/FaithlPack $name ")
                .sendTo(sender)
            sender.sendMessage("      §7$desc")
        }
        sender.sendMessage("")
    }

}