package com.faithl.pack.internal.command.impl

import com.faithl.pack.FaithlPack
import com.faithl.pack.common.inventory.InventoryUI
import com.faithl.pack.common.inventory.Pack
import org.bukkit.Bukkit
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.module.lang.sendLang

object CommandReload {

    val command = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            Pack.packList.clear()
            FaithlPack.init()
            FaithlPack.setting.reload()
            InventoryUI.openingInventory.forEach {
                InventoryUI.saveInventory(it.key)
                Bukkit.getPlayer(it.key)?.closeInventory()
            }
            sender.sendLang("Command-Reload-Info")
        }
    }

}