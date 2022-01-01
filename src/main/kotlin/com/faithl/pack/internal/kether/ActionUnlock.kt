package com.faithl.pack.internal.kether

import com.faithl.pack.common.inventory.InventoryUI
import com.faithl.pack.common.inventory.PackUI
import org.bukkit.entity.Player
import taboolib.library.xseries.XMaterial
import taboolib.module.kether.*
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import java.util.concurrent.CompletableFuture

/**
 * @author Leosouthey
 * @time 2021/11/27-22:51
 **/
class ActionUnlock {

    class Unlock(val slot: Int) : ScriptAction<Unit>() {

        override fun run(frame: ScriptFrame): CompletableFuture<Unit> {
            val player = frame.script().sender?.castSafely<Player>() ?: error("No player selected.")
            if (slot >= (InventoryUI.inventoryViewing[player]!!.size - 9)) {
                error("无效的Slot.")
            } else if ((InventoryUI.inventoryViewing[player]?.getItem(slot)?.getItemTag()?.getDeep("pack.type")
                    ?: error("该Slot下的物品无法被解锁.")) != ItemTagData("unlock")
            ) {
                error("该Slot下的物品无法被解锁.")
            } else return CompletableFuture.completedFuture(
                InventoryUI.inventoryViewing[player]?.setItem(
                    slot,
                    XMaterial.AIR.parseItem()
                )
            )
        }

    }

    companion object {
        /**
         * packViewing unlock slot material
         */
        @KetherParser(["unlock"], shared = true)
        fun parser() = scriptParser {
            Unlock(it.nextInt())
        }
    }
}