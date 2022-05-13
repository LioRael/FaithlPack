package com.faithl.pack.common.core

import com.faithl.pack.api.FaithlPackAPI
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

object Placeholder : PlaceholderExpansion {

    override val identifier: String
        get() = "faithlpack"

    override fun onPlaceholderRequest(player: Player?, args: String): String {
        if (player == null) {
            return "error"
        }
        val openingPack = FaithlPackAPI.preopeningPack.firstOrNull { it.opener == player } ?: return "error"
        when (args) {
            "page" -> {
                return openingPack.page.toString()
            }
            "pages" -> {
                return openingPack.packData.getSetting().inventory!!.getInt("pages").toString()
            }
            "unlocked" -> {
                return FaithlPackAPI.getUnlockedSize(openingPack.player, openingPack.packData).toString()
            }
        }
        return "error"
    }

}