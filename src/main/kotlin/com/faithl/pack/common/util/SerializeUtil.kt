package com.faithl.pack.common.util

import org.bukkit.inventory.Inventory
import taboolib.platform.util.deserializeToInventory
import taboolib.platform.util.serializeToByteArray
import java.util.*

fun Inventory.serializeToString(zipped: Boolean = true): String {
    return Base64.getEncoder().encodeToString(this.serializeToByteArray(zipped = zipped))
}

fun String.deserializeToInventory(zipped: Boolean = true): Inventory {
    return Base64.getDecoder().decode(this).deserializeToInventory(zipped = zipped)
}