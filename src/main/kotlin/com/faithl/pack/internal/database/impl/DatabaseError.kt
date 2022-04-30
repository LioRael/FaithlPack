package com.faithl.pack.internal.database.impl

import com.faithl.pack.common.core.PackData
import com.faithl.pack.internal.database.Database
import java.util.*

class DatabaseError(val cause: Throwable) : Database() {
    init {
        cause.printStackTrace()
    }

    override fun getPackData(uuid: UUID, packName: String): PackData {
        throw IllegalAccessError("Database initialization failed: ${cause.localizedMessage}")
    }

    override fun setPackData(uuid: UUID, packData: PackData) {
        throw IllegalAccessError("Database initialization failed: ${cause.localizedMessage}")
    }

    override fun getPackOption(uuid: UUID, packName: String, key: String): String? {
        throw IllegalAccessError("Database initialization failed: ${cause.localizedMessage}")
    }

    override fun setPackOption(uuid: UUID, packName: String, key: String, value: String) {
        throw IllegalAccessError("Database initialization failed: ${cause.localizedMessage}")
    }

}