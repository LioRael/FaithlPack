package com.faithl.pack.internal.kether;

import org.bukkit.entity.Player
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.*
import taboolib.module.nms.getName
import taboolib.platform.util.hasItem
import taboolib.platform.util.hasLore
import taboolib.platform.util.hasName
import taboolib.platform.util.takeItem
import java.util.concurrent.CompletableFuture

/**
 * @author Leosouthey
 * @time 2021/12/10-20:13
 **/
class ActionInventory {

    class Has(val type:String,val value: ParsedAction<String>,val amount:Int) : ScriptAction<Boolean>() {

        override fun run(frame: ScriptFrame): CompletableFuture<Boolean> {
            val player = frame.script().sender?.castSafely<Player>() ?: error("No player selected.")
            return CompletableFuture<Boolean>().also {
                frame.newFrame(value).run<Any?>().thenAccept { value ->
                    when (type) {
                        "name" -> {
                            it.complete(player.inventory.hasItem(amount) { itemStack ->
                                itemStack.getName().contains(value.toString())
                            })
                        }
                        "lore" -> {
                            it.complete(player.inventory.hasItem(amount) { itemStack ->
                                itemStack.hasLore(value.toString())
                            })
                        }
                    }
                    error("无效的Type.")
                }
            }
        }
    }

    class Take(val type: String, val value: ParsedAction<String>, val amount:Int) : ScriptAction<Boolean>() {

        override fun run(frame: ScriptFrame): CompletableFuture<Boolean> {
            val player = frame.script().sender?.castSafely<Player>() ?: error("No player selected.")
            return CompletableFuture<Boolean>().also {
                frame.newFrame(value).run<Any?>().thenAccept { value ->
                    when (type) {
                        "name" -> {
                            it.complete(player.inventory.takeItem(amount) { itemStack ->
                                itemStack.getName().contains(value.toString())
                            })
                        }
                        "lore" -> {
                            it.complete(player.inventory.takeItem(amount) { itemStack ->
                                itemStack.hasLore(value.toString())
                            })
                        }
                    }
                    error("无效的Type.")
                }
            }
        }
    }

    companion object {
        @KetherParser(["inventory"], shared = true)
        fun parser() = scriptParser {
            it.switch {
                case("has"){ Has(it.nextToken(),it.nextAction(),it.nextInt()) }
                case("take"){ Take(it.nextToken(),it.nextAction(),it.nextInt()) }
            }
        }
    }
}
