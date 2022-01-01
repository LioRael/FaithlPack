package com.faithl.pack.common.inventory

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.api.event.PackOpenEvent
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
import taboolib.platform.util.sendLang

class PackUI(val pack: Pack) : InventoryUI() {

    override fun build(player: Player, page: Int): Inventory {
        return buildMenu<Basic>(
            title = pack.inventoryConfig!!.getString("title")!!.colored().replacePlaceholder(player)
                .replace("{page}", page.toString())
                .replace("{pages}", pack.inventoryConfig.getInt("pages").toString())
        ) {
            handLocked(false)
            rows(pack.inventoryConfig.getInt("rows"))
        }
    }

    override fun open(player: Player, page: Int) {
        val ui = getData(player, page) ?: return
        openingInventory[player] = ui
        openingPack[player] = pack
        openingPage[player] = page
        PackOpenEvent(player, pack, page).call()
        player.openInventory(ui)
    }

    fun updateItems(player: Player, page: Int, pack: Inventory) {
        for (slot in 0 until pack.size) {
            val type = pack.getItem(slot)?.getItemTag()?.getDeep("pack.type") ?: continue
            val itemStack = getNBTItemStack(player, page, type.asString())
            pack.setItem(slot, itemStack)
        }
    }

    fun initItems(player: Player, page: Int, packInv: Inventory, defaultSize: Int = 0) {
        val rows = pack.inventoryConfig!!.getInt("rows")
        if (pack.enabledLock) {
            val unlockItemStack = getNBTItemStack(player, page, "unlock")
            for (slot in 0 + defaultSize until (rows - 1) * 9) {
                packInv.setItem(slot, unlockItemStack)
            }
        }
        val pageItemStack = getNBTItemStack(player, page, "page")
        packInv.setItem(rows * 9 - 5, pageItemStack)
        val nullItemStack = getNBTItemStack(player, page, "null")
        listOf(
            rows * 9 - 2,
            rows * 9 - 3,
            rows * 9 - 4,
            rows * 9 - 6,
            rows * 9 - 7,
            rows * 9 - 8,
            rows * 9 - 9
        ).forEach {
            packInv.setItem(it, nullItemStack)
        }
        val autoPickupItemStack = getNBTItemStack(player, page, "setting.auto-pickup")
        packInv.setItem(rows * 9 - 1, autoPickupItemStack)
    }

    fun getNBTItemStack(player: Player, page: Int, type: String): ItemStack {
        return getItemStack(player, page, type).apply {
            getItemTag().also { itemTag ->
                itemTag.putDeep("pack.type", type)
                itemTag.saveTo(this)
            }
        }
    }

    fun getItemStack(player: Player, page: Int, item: String): ItemStack {
        return buildItem(
            pack.inventoryConfig?.getString("items.${item}.display.material")?.parseToXMaterial()
                ?: XMaterial.GRAY_STAINED_GLASS
        ) {
            name = pack.inventoryConfig?.getString("items.${item}.display.name")?.colored()?.replacePlaceholder(player)
                ?.replace("{page}", page.toString())
                ?.replace("{pages}", pack.inventoryConfig.getInt("pages").toString()) ?: ""
            pack.inventoryConfig?.getStringList("items.${item}.display.lore")?.colored()?.forEach {
                lore += it.replacePlaceholder(player).replace("{page}", page.toString()).replace(
                    "{pages}",
                    pack.inventoryConfig.getInt("pages").toString()
                )
            }
            for (s in pack.inventoryConfig?.getStringList("items.${item}.display.enchants") ?: mutableListOf()) {
                if (s.isEmpty()) {
                    break
                }
                val enchant = s.split(":")[0]
                val level = Coerce.toInteger(s.split(":")[1])
                enchants[XEnchantment.valueOf(enchant).parseEnchantment()!!] =
                    (enchants[XEnchantment.valueOf(enchant).parseEnchantment()!!] ?: 0) + level
            }
            if (pack.inventoryConfig?.getBoolean("items.${item}.display.shiny") == true) {
                shiny()
            }
        }
    }

    fun getData(player: Player, page: Int): Inventory? {
        if (page > pack.inventoryConfig!!.getInt("pages") || page <= 0) {
            return null
        }
        if (pack.permission != null && !player.hasPermission(pack.permission)) {
            player.sendLang("Player-No-Permission")
            return null
        }
        val ui = build(player, page)
        val databasePack = FaithlPackAPI.getPackInventory(player, pack, page)
        if (databasePack != null) {
            ui.contents = databasePack.contents
            updateItems(player, page, ui)
        } else {
            //初始化默认槽位
            val default = pack.inventoryConfig.getInt("default-size") - page * 45
            if (default > 0) {
                initItems(player, page, ui, default)
            }
        }
        return ui
    }
}