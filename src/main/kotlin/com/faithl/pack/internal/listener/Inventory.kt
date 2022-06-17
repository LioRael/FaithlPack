package com.faithl.pack.internal.listener

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.api.event.PackClickEvent
import com.faithl.pack.api.event.PackCloseEvent
import com.faithl.pack.api.event.PackPickupEvent
import com.faithl.pack.api.event.PackPlaceItemEvent
import com.faithl.pack.common.core.PackSetting
import com.faithl.pack.internal.command.impl.CommandUnbind
import com.faithl.pack.internal.database.Database
import com.faithl.pack.internal.util.checkUpdate
import com.faithl.pack.internal.util.condition
import com.faithl.pack.internal.util.putTo
import com.faithl.pack.internal.util.sendLangIfEnabled
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.*
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.library.xseries.XSound
import taboolib.module.nms.getItemTag
import taboolib.module.nms.getName
import taboolib.platform.util.isAir

/**
 * @author Leosouthey
 * @since 2022/4/30-19:59
 **/
object Inventory {

    @SubscribeEvent
    fun e(e: PlayerJoinEvent) {
        if (e.player.isOp) {
            checkUpdate(e.player)
        }
    }

    @SubscribeEvent
    fun e(e: InventoryOpenEvent) {
        if (e.player !is Player) {
            return
        }
        val player = e.player as Player
        if (FaithlPackAPI.openingPacks.find { it.opener == player } != null) {
            return
        }
        if (e.inventory.type == InventoryType.WORKBENCH) {
            return
        }
        if (CommandUnbind.unbindPlayers.find { player.uniqueId == it } != null) {
            CommandUnbind.unbindPlayers.remove(player.uniqueId)
            return
        }
        for (item in e.inventory.storageContents) {
            if (item.isAir()) {
                continue
            }
            val type = item?.getItemTag()?.getDeep("pack.type") ?: continue
            if (type.asString() == "bind") {
                e.isCancelled = true
                Database.INSTANCE.getPackData(player.uniqueId, item.getItemTag().getDeep("pack.bind").asString())
                    .open(player, 1)
                break
            }
        }
    }

    @SubscribeEvent
    fun e(e: InventoryCloseEvent) {
        FaithlPackAPI.openingPacks.find {
            it.player == e.player
        }?.let {
            PackCloseEvent(e.player as Player, it.opener, it.packData, it.page, e.inventory).call()
            FaithlPackAPI.openingPacks.remove(it)
        }
    }

    @SubscribeEvent
    fun ce1(e: InventoryClickEvent) {
        FaithlPackAPI.openingPacks.find {
            it.player == e.whoClicked
        }?.let {
            val event = PackClickEvent(
                it.player,
                it.opener,
                it.packData,
                it.page,
                it.inventory,
                e.currentItem,
                e.isLeftClick,
                e.isRightClick
            ).call()
            if (!event) {
                e.isCancelled = true
            }
        }
    }

    @SubscribeEvent
    fun ce2(e: InventoryClickEvent) {
        FaithlPackAPI.openingPacks.find {
            it.player == e.whoClicked
        }?.let {
            val player = it.player
            if (e.action == InventoryAction.DROP_ALL_CURSOR || e.action == InventoryAction.DROP_ALL_SLOT || e.action == InventoryAction.DROP_ONE_CURSOR || e.action == InventoryAction.DROP_ONE_SLOT) {
                e.isCancelled = true
                return
            }
            val event = PackPlaceItemEvent(
                player,
                it.opener,
                it.inventory,
                it.packData,
                it.page,
                e.action,
                e.currentItem
            ).call()
            if (!event) {
                e.isCancelled = true
            }
        }
    }

    @SubscribeEvent
    // 自动拾取
    fun e(e: EntityPickupItemEvent) {
        if (e.entity !is Player) {
            return
        }
        val player = e.entity as Player
        pack@ for (pack in PackSetting.instances) {
            if (pack.sort?.getBoolean("auto-pickup.enabled") ?: continue) {
                if (!Database.INSTANCE.getPackOption(player.uniqueId, pack.name, "auto-pickup").toBoolean()) {
                    continue@pack
                }
            }
            if (pack.permission != null && !player.hasPermission(pack.permission)) {
                continue@pack
            }
            val autoPickupPermission = pack.sort.getString("auto-pickup.permission")
            if (autoPickupPermission != null && !player.hasPermission(autoPickupPermission)) {
                continue@pack
            }
            if (condition(player, pack, "ban-condition", e.item.itemStack)) {
                continue@pack
            }
            if (condition(player, pack, "condition", e.item.itemStack)) {
                val itemStack = e.item.itemStack.clone()
                page@ for (page in 1..pack.inventory!!.getInt("pages")) {
                    val packData = Database.INSTANCE.getPackData(player.uniqueId, pack.name)
                    PackPickupEvent(player, packData, page).call()
                    if (FaithlPackAPI.openingPacks.find { it.player == player } != null) {
                        player.closeInventory()
                    }
                    val inventory = packData.build(player, page)
                    itemStack.putTo(inventory)
                    packData.setPageItems(page, inventory)
                    if (itemStack.amount == 0) {
                        break@page
                    }
                    if (itemStack.amount > 0) {
                        continue@page
                    }
                }
                if (e.item.itemStack.amount - itemStack.amount != 0) {
                    player.sendLangIfEnabled(
                        "pack-auto-pickup-info",
                        e.item.itemStack.amount - itemStack.amount,
                        e.item.itemStack.getName(),
                        pack.name
                    )
                }
                if (itemStack.amount == 0) {
                    e.isCancelled = true
                    e.item.remove()
                } else {
                    e.item.itemStack = itemStack
                }
                XSound.ENTITY_ITEM_PICKUP.play(player)
                break@pack
            }
        }
    }

}