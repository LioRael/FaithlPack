package com.faithl.pack.internal.database

import com.faithl.pack.common.core.PackData
import com.faithl.pack.common.core.PlayerData
import com.faithl.pack.internal.database.impl.DatabaseError
import com.faithl.pack.internal.database.impl.DatabaseSQL
import com.faithl.pack.internal.database.impl.DatabaseSQLite
import org.bukkit.event.player.PlayerLoginEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.chat.colored
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Leosouthey
 * @since 2022/4/30-18:19
 **/
abstract class Database {

    abstract fun getPackData(uuid: UUID, packName: String): PackData

    abstract fun setPackData(uuid: UUID, packData: PackData)

    abstract fun getPackOption(uuid: UUID, packName: String, key: String): String?

    abstract fun setPackOption(uuid: UUID, packName: String, key: String, value: String)

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
                e.kickMessage = "&cERROR! &rThe &bFaithlPack&r database failed to initialize.".colored()
            }
        }

        val cache = ConcurrentHashMap<UUID, PlayerData>()
    }

}