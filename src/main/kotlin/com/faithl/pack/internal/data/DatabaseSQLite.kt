package com.faithl.pack.internal.data

import com.faithl.pack.common.inventory.Pack
import org.bukkit.entity.Player
import taboolib.common.io.newFile
import taboolib.common.platform.function.getDataFolder
import taboolib.module.database.*

class DatabaseSQLite: Database() {
    val host = newFile(getDataFolder(), "data.db").getHost()

    val tablePack = Table("faithlpack_data", host) {
        add("player") {
            type(ColumnTypeSQLite.TEXT, 36)
        }
        add("pack") {
            type(ColumnTypeSQLite.TEXT, 128)
        }
        add("page") {
            type(ColumnTypeSQLite.INTEGER)
        }
        add("value") {
            type(ColumnTypeSQLite.TEXT)
        }
    }

    val tableSettings = Table("faithlpack_setting",host){
        add("player") {
            type(ColumnTypeSQLite.TEXT, 36)
        }
        add("pack") {
            type(ColumnTypeSQLite.TEXT, 128)
        }
        add("auto-pickup") {
            type(ColumnTypeSQLite.INTEGER)
        }
    }

    val dataSource = host.createDataSource()

    init {
        tablePack.createTable(dataSource)
        tableSettings.createTable(dataSource)
    }

    override fun getPack(player: Player, pack: Pack, page: Int): String? {
        return tablePack.select(dataSource) {
            where("player" eq player.uniqueId.toString() and ("pack" eq pack.name!!) and ("page" eq page))
            rows("value")
        }.firstOrNull  {
            getString("value")
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
                value(player.uniqueId.toString(), pack.name!!,page, value)
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