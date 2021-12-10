package com.faithl.pack.internal.data

import com.faithl.pack.FaithlPack
import com.faithl.pack.common.inventory.Pack
import com.faithl.pack.common.util.deserializeToInventory
import org.bukkit.entity.Player
import taboolib.module.database.*

class DatabaseSQL: Database() {
    val host = FaithlPack.setting.getHost("Database.source.MYSQL")
    val name = "faithlpack"

    val tablePack = Table("${name}_data", host) {
        add { id() }
        add("player") {
            type(ColumnTypeSQL.VARCHAR, 36)
        }
        add("pack") {
            type(ColumnTypeSQL.VARCHAR, 128)
        }
        add("page") {
            type(ColumnTypeSQL.INT)
        }
        add("value") {
            type(ColumnTypeSQL.LONGTEXT)
        }
    }

    val tableSettings = Table("${name}_setting",host){
        add { id() }
        add("player") {
            type(ColumnTypeSQL.VARCHAR, 36)
        }
        add("pack") {
            type(ColumnTypeSQL.VARCHAR, 128)
        }
        add("auto-pickup") {
            type(ColumnTypeSQL.BOOLEAN)
        }
    }

    val dataSource = host.createDataSource()

    init {
        tablePack.createTable(dataSource)
        tableSettings.createTable(dataSource)
    }

    override fun getPack(player: Player, pack: Pack) {
        return tablePack.select(dataSource) {
            where("player" eq player.uniqueId.toString() and ("pack" eq pack.name!!))
            rows("value","page")
        }.forEach {
            SerializedInventory.getInstance(player).inventories[mutableMapOf(pack to getInt("page"))] = getString("value").deserializeToInventory()
        }
    }

    override fun setPack(player: Player, pack: Pack, page: Int, value: String) {
        if (tablePack.find(dataSource) { where("player" eq player.uniqueId.toString() and ("pack" eq pack.name!!) and ("page" eq page)) }) {
            tablePack.update(dataSource) {
                where("player" eq player.uniqueId.toString() and ("pack" eq pack.name!!) and ("page" eq page))
                set("value", value)
            }
        } else {
            tablePack.insert(dataSource, "player", "pack", "page", "value") {
                value(player.uniqueId.toString(), pack.name!!, page, value)
            }
        }
    }

    override fun getAutoPickup(player: Player, pack: Pack): Boolean {
        return tableSettings.select(dataSource) {
            where("player" eq player.uniqueId.toString() and ("pack" eq pack.name!!))
            rows("auto-pickup")
        }.firstOrNull {
            getBoolean("auto-pickup")
        } ?: getDefaultAutoPickup(player,pack)
    }

    override fun setAutoPickup(player: Player, pack: Pack, autoPick: Boolean) {
        if (tableSettings.find(dataSource) { where("player" eq player.uniqueId.toString() and ("pack" eq pack.name!!)) }) {
            tableSettings.update(dataSource) {
                where("player" eq player.uniqueId.toString() and ("pack" eq pack.name!!))
                set("auto-pickup", autoPick)
            }
        } else {
            tableSettings.insert(dataSource, "player", "pack", "auto-pickup") {
                value(player.uniqueId.toString(), pack.name!!, autoPick)
            }
        }
    }
}