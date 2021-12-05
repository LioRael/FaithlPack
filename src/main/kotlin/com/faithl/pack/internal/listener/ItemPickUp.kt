package com.faithl.pack.internal.listener

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.common.inventory.InventoryUI
import com.faithl.pack.common.inventory.Pack
import com.faithl.pack.common.inventory.PackUI
import com.faithl.pack.common.util.putItem
import com.faithl.pack.internal.data.Database
import ink.ptms.zaphkiel.ZaphkielAPI
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.util.asList
import taboolib.library.xseries.XSound
import taboolib.module.nms.getName
import taboolib.platform.util.hasLore
import taboolib.platform.util.hasName
import taboolib.platform.util.sendLang

object ItemPickUp {

    @SubscribeEvent
    fun e(e: EntityPickupItemEvent){
        if (e.entity !is Player){
            return
        }
        val player = e.entity as Player
        pack@for (pack in Pack.packList){
            if (pack.autoPickup?.getBoolean("enabled") ?: return)
            if (!Database.INSTANCE.getAutoPickup(player,pack)){
                return
            }
            if (pack.permission != null && !player.hasPermission(pack.permission)){
                return
            }
            val autoPickupPermission = pack.autoPickup.getString("permission")
            if (autoPickupPermission !=null && !player.hasPermission(autoPickupPermission)){
                return
            }
            if (condition(pack, e.item.itemStack)){
                val itemStack = e.item.itemStack.clone()
                page@for (page in 1..pack.inventoryConfig!!.getInt("pages")){
                    if (pack.ui is PackUI){
                        if (InventoryUI.inventoryViewing[player] != null){
                            player.closeInventory()
                        }
                        val newPack = (pack.ui as PackUI).getData(player,page)
                        newPack!!.putItem(itemStack)
                        FaithlPackAPI.setPack(player,pack,page,newPack)
                        if (itemStack.amount == 0){
                            break@page
                        }
                        if (itemStack.amount > 0){
                            continue@page
                        }
                    }
                }
                if (e.item.itemStack.amount - itemStack.amount != 0){
                    player.sendLang("Pack-Auto-Pick-Info",e.item.itemStack.amount - itemStack.amount,e.item.itemStack.getName(), pack.name!!)
                }
                if (itemStack.amount == 0){
                    e.isCancelled = true
                    e.item.remove()
                }else{
                    e.item.itemStack = itemStack
                }
                XSound.ENTITY_ITEM_PICKUP.play(player)
                break@pack
            }
        }
    }

    fun condition(pack:Pack,item:ItemStack):Boolean{
        val conditions = pack.autoPickup?.getConfigurationSection("condition") ?: return true
        when(conditions.getString("mode")){
            "name" -> {
                pack.autoPickup["condition.value"]?.asList()?.forEach{ value ->
                    if (item.hasName(value)){
                        return true
                    }
                    if (item.getName().contains(value)){
                        return true
                    }
                }
            }
            "lore" -> {
                if (item.itemMeta == null){
                    return false
                }
                pack.autoPickup["condition.value"]?.asList()?.forEach{ value ->
                    if (item.hasLore(value)){
                        return true
                    }
                }
            }
            "zap","zaphkiel" -> {
                val itemStream = ZaphkielAPI.read(item)
                if (itemStream.isExtension()){
                    val type = itemStream.getZaphkielData().getDeep("faithlpack.type")?.asString() ?:return false
                    pack.autoPickup["condition.value"]?.asList()?.forEach{ value ->
                        if (type == value){
                            return true
                        }
                    }
                }
            }
        }
        return false
    }
}