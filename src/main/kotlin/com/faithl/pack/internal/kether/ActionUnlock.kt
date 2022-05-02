package com.faithl.pack.internal.kether

import com.faithl.pack.api.FaithlPackAPI
import org.bukkit.entity.Player
import taboolib.module.kether.*
import java.util.concurrent.CompletableFuture

/**
 * @author Leosouthey
 * @since 2021/11/27-22:51
 **/
class ActionUnlock {

    class Unlock(val size: Int) : ScriptAction<Unit>() {

        override fun run(frame: ScriptFrame): CompletableFuture<Unit> {
            val player = frame.script().sender?.castSafely<Player>() ?: error("No player selected.")
            val openingPack = FaithlPackAPI.openingPacks.first { it.player == player }
            return CompletableFuture.completedFuture(
                FaithlPackAPI.unlock(player, openingPack.packData, size)
            )
        }

    }

    companion object {

        /**
         * openingPack unlock slot material
         */
        @KetherParser(["unlock"], shared = true)
        fun parser() = scriptParser {
            Unlock(it.nextInt())
        }

    }

}