package com.faithl.pack.internal.listener

import com.faithl.pack.common.inventory.InventoryUI
import com.faithl.pack.common.inventory.PackUI
import com.faithl.pack.internal.data.Database
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.util.asList
import taboolib.module.kether.KetherShell
import taboolib.module.nms.getItemTag
import taboolib.platform.util.isAir
import taboolib.platform.util.sendLang

object InventoryClick {

    @SubscribeEvent
    fun e(e: InventoryClickEvent) {
        if (e.whoClicked !is Player) {
            return
        }
        val player = e.whoClicked
        if (InventoryUI.openingInventory[player] == null) {
            return
        }
        if (InventoryUI.openingInventory[player] != e.inventory) {
            return
        }
        val item = e.currentItem ?: return
        if (item.isAir()) {
            return
        }
        val type = item.getItemTag().getDeep("pack.type") ?: return
        val pack = InventoryUI.openingPack[player]
        e.isCancelled = true
        when (type.asString()) {
            "page" -> {
                val page = InventoryUI.openingPage[player]!!
                if (e.isLeftClick) {
                    PackUI(pack!!).open(player as Player, page + 1)
                } else if (e.isRightClick) {
                    PackUI(pack!!).open(player as Player, page - 1)
                }
            }
            "unlock" -> {
                if (e.isLeftClick) {
                    KetherShell.eval(
                        source = pack!!.inventoryConfig!!["items.unlock.action.left-click"]?.toString()
                            ?.replace("@clickedSlot", e.slot.toString(), true)?.asList() ?: return,
                        sender = adaptPlayer(player),
                        namespace = listOf("faithlpack", "faithlpack-internal")
                    )
                } else if (e.isRightClick) {
                    KetherShell.eval(
                        source = pack!!.inventoryConfig!!["items.unlock.action.right-click"]?.toString()
                            ?.replace("@clickedSlot", e.slot.toString(), true)?.asList() ?: return,
                        sender = adaptPlayer(player),
                        namespace = listOf("faithlpack", "faithlpack-internal")
                    )
                }
            }
            "setting.auto-pickup" -> {
                if (Database.INSTANCE.getAutoPickup(player as Player, pack!!)) {
                    Database.INSTANCE.setAutoPickup(player, pack, false)
                    player.sendLang("Pack-Auto-Pick-Off", pack.name!!)
                } else {
                    Database.INSTANCE.setAutoPickup(player, pack, true)
                    player.sendLang("Pack-Auto-Pick-On", pack.name!!)
                }
            }
        }
    }

}