package com.faithl.pack.common.item

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.common.core.PackData
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.common5.Coerce
import taboolib.library.xseries.XEnchantment
import taboolib.library.xseries.XMaterial
import taboolib.library.xseries.parseToXMaterial
import taboolib.module.chat.colored
import taboolib.module.nms.getItemTag
import taboolib.module.ui.buildMenu
import taboolib.module.ui.type.Basic
import taboolib.platform.compat.replacePlaceholder
import taboolib.platform.util.buildItem

object ItemBuilder {

    fun buildInventory(player: Player, packData: PackData, page: Int, rows: Int): Inventory {
        val inventory = buildMenu<Basic>(
            packData.getSetting().inventory?.getString("title")?.colored()?.replacePlaceholder(player) ?: packData.name
        ) {
            handLocked(false)
            rows(rows * 9)
        }
        if (packData.getSetting().lock) {
            val unlockedSize = FaithlPackAPI.getUnlockedSize(player, packData)
            val lockedItemStack = getNBTItemStack(player, packData, page, "locked")
            for (slot in 0 until inventory.size - 9) {
                inventory.setItem(slot, lockedItemStack)
            }
            val deadline = if (unlockedSize - (page - 1) * (inventory.size - 9) > (inventory.size - 9)) {
                (inventory.size - 9)
            } else {
                unlockedSize - (page - 1) * (inventory.size - 9)
            }
            if (deadline > 0) {
                for (slot in 0 until deadline) {
                    inventory.clear(slot)
                }
            }
        }
        val pageItemStack = getNBTItemStack(player, packData, page, "page")
        inventory.setItem(rows * 9 - 5, pageItemStack)
        val nullItemStack = getNBTItemStack(player, packData, page, "empty")
        listOf(
            rows * 9 - 2,
            rows * 9 - 3,
            rows * 9 - 4,
            rows * 9 - 6,
            rows * 9 - 7,
            rows * 9 - 8,
            rows * 9 - 9
        ).forEach {
            inventory.setItem(it, nullItemStack)
        }
        val autoPickupItemStack = getNBTItemStack(player, packData, page, "setting.auto-pickup")
        inventory.setItem(rows * 9 - 1, autoPickupItemStack)
        packData.getPageItems(page).forEach {
            inventory.setItem(it.key, it.value)
        }
        return inventory
    }

    fun getNBTItemStack(player: Player, packData: PackData, page: Int, type: String): ItemStack {
        return getItemStack(player, packData, page, type).apply {
            getItemTag().also { itemTag ->
                itemTag.putDeep("pack.type", type)
                itemTag.saveTo(this)
            }
        }
    }

    fun getItemStack(player: Player, packData: PackData, page: Int, item: String): ItemStack {
        val packSetting = packData.getSetting()
        return buildItem(
            packSetting.inventory?.getString("items.${item}.display.material")?.parseToXMaterial()
                ?: XMaterial.GRAY_STAINED_GLASS
        ) {
            name = packSetting.inventory?.getString("items.${item}.display.name")?.colored()?.replacePlaceholder(player)
                ?.replace("{page}", page.toString())
                ?.replace("{pages}", packSetting.inventory.getInt("pages").toString()) ?: ""
            packSetting.inventory?.getStringList("items.${item}.display.lore")?.colored()?.forEach {
                lore += it.replacePlaceholder(player).replace("{page}", page.toString()).replace(
                    "{pages}",
                    packSetting.inventory.getInt("pages").toString()
                )
            }
            for (s in packSetting.inventory?.getStringList("items.${item}.display.enchants") ?: mutableListOf()) {
                if (s.isEmpty()) {
                    break
                }
                val enchant = s.split(":")[0]
                val level = Coerce.toInteger(s.split(":")[1])
                enchants[XEnchantment.valueOf(enchant).enchant!!] =
                    (enchants[XEnchantment.valueOf(enchant).enchant!!] ?: 0) + level
            }
            if (packSetting.inventory?.getBoolean("items.${item}.display.shiny") == true) {
                shiny()
            }
        }
    }

}