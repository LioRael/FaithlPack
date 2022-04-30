package com.faithl.pack.common.core

import taboolib.library.configuration.ConfigurationSection

/**
 * @author Leosouthey
 * @since 2022/4/30-17:22
 **/
class PackSetting(val name: String, val data: ConfigurationSection) {

    companion object {
        val instances = mutableListOf<PackSetting>()
    }

    init {
        instances.forEach {
            if (it.name == this.name) {
                throw Exception("Duplicate name")
            }
        }
        instances.add(this)
    }

    val inventory = data.getConfigurationSection("inventory")
    val sort = data.getConfigurationSection("sort")

    val permission = data.getString("use-permission")
    val lock = data.getBoolean("lock")
    val rows = inventory?.getInt("rows") ?: 6

}