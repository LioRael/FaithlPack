package com.faithl.pack.internal.command.impl

import com.faithl.pack.FaithlPack
import com.faithl.pack.common.inventory.InventoryUI
import com.faithl.pack.common.inventory.Pack
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand

object CommandSave {

    val command = subCommand {
        execute<Player> { sender, _, _ ->
            InventoryUI.saveInventory(sender)
        }
    }

}