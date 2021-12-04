package com.faithl.pack.internal.data

import com.faithl.pack.common.inventory.Pack
import org.bukkit.entity.Player

class DatabaseError(val cause: Throwable): Database(){
    init {
        cause.printStackTrace()
    }

    override fun getPack(player: Player, pack: Pack, page: Int): String? {
        throw IllegalAccessError("Database initialization failed: ${cause.localizedMessage}")
    }

    override fun setPack(player: Player, pack: Pack, page: Int, value: String) {
        throw IllegalAccessError("Database initialization failed: ${cause.localizedMessage}")
    }

    override fun getAutoPickup(player: Player, pack: Pack): Boolean {
        throw IllegalAccessError("Database initialization failed: ${cause.localizedMessage}")
    }

    override fun setAutoPickup(player: Player, pack: Pack, autoPick: Boolean) {
        throw IllegalAccessError("Database initialization failed: ${cause.localizedMessage}")
    }
}