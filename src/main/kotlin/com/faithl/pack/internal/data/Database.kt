package com.faithl.pack.internal.data

import com.faithl.pack.common.inventory.Pack
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerLoginEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.chat.colored

abstract class Database {

    abstract fun getPack(player: Player, pack: Pack)

    abstract fun setPack(player: Player, pack: Pack, page: Int, value: String)

    abstract fun getAutoPickup(player: Player, pack: Pack): Boolean

    abstract fun setAutoPickup(player: Player, pack: Pack, autoPick: Boolean)

    companion object {
        val INSTANCE by lazy {
            try {
                when (Type.INSTANCE) {
                    Type.MYSQL -> DatabaseSQL()
                    Type.SQLITE -> DatabaseSQLite()
                }
            } catch (e: Throwable) {
                DatabaseError(e)
            }
        }

        @SubscribeEvent
        fun e(e: PlayerLoginEvent) {
            if (INSTANCE is DatabaseError) {
                e.result = PlayerLoginEvent.Result.KICK_OTHER
                e.kickMessage = "&4&loERROR! &r&oThe &4&lFaithlPack&r&o database failed to initialize.".colored()
            }
        }
    }

    fun getDefaultAutoPickup(player: Player, pack: Pack): Boolean {
        val default = pack.sort?.getBoolean("auto-pickup.player-default-enabled")
        if (default != null) {
            INSTANCE.setAutoPickup(player, pack, default)
            return default
        }
        return false
    }
}