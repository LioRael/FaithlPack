package com.faithl.pack.common.util

import com.faithl.pack.common.inventory.Pack
import ink.ptms.zaphkiel.ZaphkielAPI
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.common.util.asList
import taboolib.module.nms.getItemTag
import taboolib.module.nms.getName
import taboolib.platform.util.hasLore
import taboolib.platform.util.isAir

/**
 * 容器物品放置工具
 * 若容器中物品已满,将会返回剩余的数量的物品堆
 *
 * @author Leosouthey
 * @time 2021/12/4-19:04
 * @param item 物品堆
 */
fun Inventory.putItem(item: ItemStack):ItemStack{
    if (contents.isNotEmpty()){
        for (inventoryItem in contents){
            if (inventoryItem == null){
                continue
            }
            if (inventoryItem.getItemTag() == item.getItemTag()){
                val remainSize = inventoryItem.type.maxStackSize - inventoryItem.amount
                if (remainSize > 0) {
                    val itemCache = item.clone()
                    if (itemCache.amount >= remainSize){
                        itemCache.amount = remainSize
                    }
                    addItem(itemCache)
                    item.amount -= itemCache.amount
                }
                if (item.amount == 0){
                    break
                }
            }
        }
    }
    if (item.amount > 0){
        for (slot in 0 until size){
            if (getItem(slot).isAir()){
                addItem(item)
                item.amount = 0
                break
            }
        }
    }
    return item
}

fun condition(pack: Pack, item:ItemStack):Boolean{
    val conditions = pack.sort?.getConfigurationSection("condition") ?: return true
    when(conditions.getString("mode")){
        "name" -> {
            pack.sort["condition.value"]?.asList()?.forEach{ value ->
                if (item.getName().contains(value)){
                    return true
                }
            }
        }
        "lore" -> {
            if (item.itemMeta == null){
                return false
            }
            pack.sort["condition.value"]?.asList()?.forEach{ value ->
                if (item.hasLore(value)){
                    return true
                }
            }
        }
        "zap","zaphkiel" -> {
            val itemStream = ZaphkielAPI.read(item)
            if (itemStream.isExtension()){
                val type = itemStream.getZaphkielData().getDeep("faithlpack.type")?.asString() ?:return false
                pack.sort["condition.value"]?.asList()?.forEach{ value ->
                    if (type == value){
                        return true
                    }
                }
            }
        }
    }
    return false
}
