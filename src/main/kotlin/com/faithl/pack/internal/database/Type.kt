package com.faithl.pack.internal.database

import com.faithl.pack.FaithlPack

/**
 * @author Leosouthey
 * @since 2022/4/30-18:22
 **/
enum class Type {

    SQLITE, MYSQL;

    companion object {

        val INSTANCE: Type by lazy {
            try {
                valueOf(FaithlPack.setting.getString("database.type", "")!!.uppercase())
            } catch (ignored: Throwable) {
                SQLITE
            }
        }
    }

}