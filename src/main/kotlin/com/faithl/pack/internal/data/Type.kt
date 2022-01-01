package com.faithl.pack.internal.data

import com.faithl.pack.FaithlPack

enum class Type {

    SQLITE, MYSQL;

    companion object {

        val INSTANCE: Type by lazy {
            try {
                valueOf(FaithlPack.setting.getString("Database.type", "")!!.uppercase())
            } catch (ignored: Throwable) {
                SQLITE
            }
        }
    }
}