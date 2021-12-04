package com.faithl.pack.common.util

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.getItemTag
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
