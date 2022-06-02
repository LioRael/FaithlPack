package com.faithl.pack.internal.util

import com.faithl.pack.common.core.PackSetting
import ink.ptms.zaphkiel.ZaphkielAPI
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.common.util.asList
import taboolib.library.xseries.parseToMaterial
import taboolib.module.nms.getI18nName
import taboolib.module.nms.getItemTag
import taboolib.module.nms.getName
import taboolib.platform.util.hasLore
import taboolib.platform.util.isAir

/**
 * 容器物品放置工具
 * 若容器中物品已满,将会返回剩余的数量的物品堆
 *
 * @author Leosouthey
 * @since 2021/12/4-19:04
 * @param inventory 容器
 * @return 物品堆
 */
fun ItemStack.putTo(inventory: Inventory): ItemStack {
    if (inventory.contents.isNotEmpty()) {
        for (inventoryItem in inventory.contents) {
            if (inventoryItem.isAir()) {
                continue
            }
            val invLore = inventoryItem.itemMeta?.lore ?: mutableListOf()
            val lore = this.itemMeta?.lore ?: mutableListOf()
            if (inventoryItem.getName() == this.getName() && invLore == lore && inventoryItem.getItemTag() == this.getItemTag()) {
                val remainSize = inventoryItem.type.maxStackSize - inventoryItem.amount
                if (remainSize > 0) {
                    val itemCache = this.clone()
                    if (itemCache.amount >= remainSize) {
                        itemCache.amount = remainSize
                    }
                    inventory.addItem(itemCache)
                    this.amount -= itemCache.amount
                }
                if (this.amount == 0) {
                    break
                }
            }
        }
    }
    if (this.amount > 0) {
        for (slot in 0 until inventory.size) {
            if (inventory.getItem(slot).isAir()) {
                inventory.addItem(this)
                this.amount = 0
                break
            }
        }
    }
    return this
}

fun condition(player: Player, pack: PackSetting, item: ItemStack): Boolean {
    val conditions = pack.sort?.getMapList("condition") ?: return true
    for (condition in conditions) {
        when (condition["mode"]) {
            "name" -> {
                condition["value"]?.asList()?.forEach { value ->
                    if (item.getI18nName(player).contains(value)) {
                        return true
                    }
                }
            }

            "lore" -> {
                if (item.itemMeta == null) {
                    return false
                }
                condition["value"]?.asList()?.forEach { value ->
                    if (item.hasLore(value)) {
                        return true
                    }
                }
            }

            "zap", "zaphkiel" -> {
                val itemStream = ZaphkielAPI.read(item)
                if (itemStream.isExtension()) {
                    val type = itemStream.getZaphkielData().getDeep("faithlpack.type")?.asString() ?: return false
                    condition["value"]?.asList()?.forEach { value ->
                        if (type == value) {
                            return true
                        }
                    }
                }
            }

            "id", "type", "material" -> {
                condition["value"]?.asList()?.forEach { value ->
                    if (value.parseToMaterial() == item.type) {
                        return true
                    }
                }
            }

            "nbt-v" -> {
                if (item.getItemTag().size <= 0) {
                    return false
                }
                val list = condition["value"]?.asList() ?: listOf()
                return item.getItemTag().values.map { it.asString() }.containsAll(list)
            }

            "nbt-k" -> {
                if (item.getItemTag().size <= 0) {
                    return false
                }
                val list = condition["value"]?.asList() ?: listOf()
                return item.getItemTag().keys.containsAll(list)
            }
        }
    }
    return false
}
