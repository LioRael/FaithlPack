package com.faithl.pack.api

import com.faithl.pack.common.core.OpeningPack
import com.faithl.pack.common.core.PackData
import com.faithl.pack.common.core.PreopeningPack
import com.faithl.pack.internal.database.Database
import org.bukkit.entity.Player
import taboolib.module.nms.getItemTag
import taboolib.platform.util.takeItem

object FaithlPackAPI {

    val openingPacks = mutableListOf<OpeningPack>()
    val preopeningPack = mutableListOf<PreopeningPack>()

    /**
     * 打开仓库
     *
     * @param player 玩家
     * @param data 仓库数据
     * @param page 页数
     */
    fun open(player: Player, data: PackData, page: Int, opener: Player = player) {
        data.open(player, page, opener)
    }

    fun save(player: Player, packData: PackData) {
        Database.INSTANCE.setPackData(player.uniqueId, packData)
    }

    fun unlock(player: Player, data: PackData, size: Int = 1) {
        val value = getUnlockedSize(player, data)
        Database.INSTANCE.setPackOption(player.uniqueId, data.name, "unlocked-size", (value + size).toString())
        openingPacks.firstOrNull { it.player == player }?.inventory?.takeItem {
            it.getItemTag().getDeep("pack.type").toString() == "locked"
        }
    }

    fun getUnlockedSize(player: Player, data: PackData): Int {
        return Database.INSTANCE.getPackOption(player.uniqueId, data.name, "unlocked-size")?.toInt()
            ?: (data.getSetting().inventory?.getInt("default-size") ?: 0)
    }

}