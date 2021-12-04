package com.faithl.pack.common.inventory

import com.faithl.pack.api.FaithlPackAPI
import com.faithl.pack.common.util.deserializeToInventory
import com.faithl.pack.internal.data.Database
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

class PackUI(val pack:Pack): InventoryUI() {

    override fun build(player: Player, page: Int): Inventory{
        return buildMenu<Basic>(title = pack.inventoryConfig!!.getString("title")!!.colored().replacePlaceholder(player)
            .replace("{page}",page.toString())
            .replace("{pages}",pack.inventoryConfig.getInt("pages").toString())) {
            handLocked(false)
            rows(pack.inventoryConfig.getInt("rows"))
        }
    }

    override fun open(player:Player, page:Int){
        val ui = getData(player,page) ?: return
        player.openInventory(ui)
        inventoryViewing[player] = ui
        packViewing[player] = pack
        packPageViewing[player] = page
    }

    fun updateItems(pack: Inventory) {
        for (slot in 0 until pack.size){
            val type = pack.getItem(slot)?.getItemTag()?.getDeep("pack.type") ?: continue
            val itemStack = getNBTItemStack(type.asString())
            pack.setItem(slot,itemStack)
        }
    }

    fun initItems(packInv: Inventory, defaultSize:Int = 0){
        val rows = pack.inventoryConfig!!.getInt("rows")
        if(pack.enabledLock){
            val unlockItemStack = getNBTItemStack("unlock")
            for (slot in 0+defaultSize until (rows - 1) * 9) {
                packInv.setItem(slot,unlockItemStack)
            }
        }
        val pageItemStack = getNBTItemStack("page")
        packInv.setItem(rows * 9 - 5,pageItemStack)
        val nullItemStack = getNBTItemStack("null")
        listOf(rows * 9 -2,rows *9 - 3,rows * 9 - 4,rows * 9 - 6,rows * 9 - 7,rows * 9 - 8,rows * 9 - 9).forEach {
            packInv.setItem(it,nullItemStack)
        }
        val autoPickupItemStack = getNBTItemStack("setting.auto-pickup")
        packInv.setItem(rows * 9 -1,autoPickupItemStack)
    }

    fun getNBTItemStack(type:String):ItemStack{
        return getItemStack(type).apply {
            getItemTag().also { itemTag ->
                itemTag.putDeep("pack.type",type)
                itemTag.saveTo(this)
            }
        }
    }

    fun getItemStack(item:String): ItemStack {
        return buildItem(pack.inventoryConfig?.getString("items.${item}.display.material")?.parseToXMaterial() ?:XMaterial.GRAY_STAINED_GLASS){
            name = pack.inventoryConfig?.getString("items.${item}.display.name")?.colored() ?:""
            lore += pack.inventoryConfig?.getStringList("items.${item}.display.lore")?.colored() ?: mutableListOf()
            for (s in pack.inventoryConfig?.getStringList("items.${item}.display.enchants") ?: mutableListOf()) {
                if (s.isEmpty()){
                    break
                }
                val enchant = s.split(":")[0]
                val level = Coerce.toInteger(s.split(":")[1])
                enchants[XEnchantment.valueOf(enchant).parseEnchantment()!!] =
                    (enchants[XEnchantment.valueOf(enchant).parseEnchantment()!!] ?: 0) + level
            }
            if (pack.inventoryConfig?.getBoolean("items.${item}.display.shiny") == true){
                shiny()
            }
        }
    }

    fun getData(player: Player,page: Int):Inventory?{
        if (page > pack.inventoryConfig!!.getInt("pages") || page<=0){
            return null
        }
        if (pack.permission !=null && !player.hasPermission(pack.permission)){
            player.sendLang("Player-No-Permission")
            return null
        }
        val ui = build(player,page)
        val databasePack = FaithlPackAPI.getPackInventory(player,pack,page)
        if (databasePack!=null){
            ui.contents = databasePack.contents
            updateItems(ui)
        }else{
            if (page == 1){
                initItems(ui, pack.inventoryConfig.getInt("default-size"))
            }else{
                initItems(ui)
            }
        }
        return ui
    }
}