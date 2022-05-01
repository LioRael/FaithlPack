package com.faithl.pack.internal.database.impl

import com.faithl.pack.common.core.PackData
import com.faithl.pack.common.core.PlayerData
import com.faithl.pack.internal.database.Database
import com.faithl.pack.internal.util.base64ToPackData
import com.faithl.pack.internal.util.toBase64
import taboolib.common.io.newFile
import taboolib.common.platform.function.getDataFolder
import taboolib.module.database.ColumnTypeSQLite
import taboolib.module.database.Table
import taboolib.module.database.getHost
import java.util.*

/**
 * @author Leosouthey
 * @since 2022/4/30-18:48
 **/
class DatabaseSQLite : Database() {
    val host = newFile(getDataFolder(), "data.db").getHost()
    val name = "faithlpack"

    val tablePack = Table("${name}_data", host) {
        add("owner") {
            type(ColumnTypeSQLite.TEXT, 36)
        }
        add("pack") {
            type(ColumnTypeSQLite.TEXT, 128)
        }
        add("value") {
            type(ColumnTypeSQLite.TEXT)
        }
    }

    val tableOptions = Table("${name}_options", host) {
        add { id() }
        add("owner") {
            type(ColumnTypeSQLite.TEXT, 36)
        }
        add("pack") {
            type(ColumnTypeSQLite.TEXT, 128)
        }
        add("key") {
            type(ColumnTypeSQLite.TEXT, 128)
        }
        add("value") {
            type(ColumnTypeSQLite.TEXT, 256)
        }
    }

    val dataSource = host.createDataSource()

    init {
        tablePack.createTable(dataSource)
        tableOptions.createTable(dataSource)
    }

    override fun getPackData(uuid: UUID, packName: String): PackData {
        val result = cache.computeIfAbsent(uuid) {
            PlayerData(tablePack.select(dataSource) {
                where("owner" eq uuid.toString())
                rows("value", "pack")
            }.map {
                PackData(getString("pack"), getString("value").base64ToPackData())
            }.toMutableList())
        }
        return result.data.find {
            it.name == packName
        } ?: PlayerData.createPackData(result, PackData(packName))
    }

    override fun setPackData(uuid: UUID, packData: PackData) {
        if (tablePack.find(dataSource) { where("owner" eq uuid.toString() and ("pack" eq packData.name)) }) {
            tablePack.update(dataSource) {
                where("owner" eq uuid.toString() and ("pack" eq packData.name))
                set("value", packData.data.toBase64())
            }
        } else {
            tablePack.insert(dataSource, "owner", "pack", "value") {
                value(uuid.toString(), packData.name, packData.data.toBase64())
            }
        }
    }

    override fun getPackOption(uuid: UUID, packName: String, key: String): String? {
        return tableOptions.select(dataSource) {
            where("owner" eq uuid.toString() and ("pack" eq packName) and ("key" eq key))
            rows("value")
        }.firstOrNull {
            getString("value")
        }
    }

    override fun setPackOption(uuid: UUID, packName: String, key: String, value: String) {
        if (tableOptions.find(dataSource) { where("owner" eq uuid.toString() and ("pack" eq packName) and ("key" eq key)) }) {
            tableOptions.update(dataSource) {
                where("owner" eq uuid.toString() and ("pack" eq packName) and ("key" eq key))
                set("value", value)
            }
        } else {
            tableOptions.insert(dataSource, "owner", "pack", "key", "value") {
                value(uuid.toString(), packName, key, value)
            }
        }
    }
}