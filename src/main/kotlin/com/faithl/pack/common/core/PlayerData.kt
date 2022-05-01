package com.faithl.pack.common.core

/**
 * @author Leosouthey
 * @since 2022/5/1-15:25
 **/
class PlayerData(val data: MutableList<PackData>) {

    companion object {
        fun createPackData(playerData: PlayerData, packData: PackData): PackData {
            playerData.data.add(packData)
            return packData
        }
    }
}