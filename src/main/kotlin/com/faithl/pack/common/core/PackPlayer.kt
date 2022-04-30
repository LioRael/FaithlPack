package com.faithl.pack.common.core

import com.faithl.pack.internal.database.Database
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Leosouthey
 * @since 2022/4/30-18:00
 **/
class PackPlayer(val uuid: UUID, val data: MutableList<PackData>) {

    companion object {
        val instances = mutableListOf<PackPlayer>()

        fun match(player: Player): PackPlayer {
            return instances.find { it.uuid == player.uniqueId } ?: PackPlayer(player.uniqueId, initPackData(player))
        }

        private fun initPackData(player: Player): MutableList<PackData> {
            val list = mutableListOf<PackData>()
            PackSetting.instances.forEach {
                list += Database.INSTANCE.getPackData(player.uniqueId, it.name)
            }
            return list
        }
    }

    init {
        instances.add(this)
    }

}