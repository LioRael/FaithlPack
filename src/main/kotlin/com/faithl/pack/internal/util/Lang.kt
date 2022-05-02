package com.faithl.pack.internal.util

import com.faithl.pack.FaithlPack
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.module.lang.sendLang
import taboolib.platform.util.sendLang

/**
 * @author Leosouthey
 * @since 2022/5/2 21:11
 **/
val enable = FaithlPack.setting.getBoolean("options.message")

fun Player.sendLangIfEnabled(lang: String, vararg args: Any) {
    if (enable) {
        this.sendLang(lang, *args)
    }
}

fun ProxyCommandSender.sendLangIfEnabled(lang: String, vararg args: Any) {
    if (enable) {
        this.sendLang(lang, *args)
    }
}

fun ProxyPlayer.sendLangIfEnabled(lang: String, vararg args: Any) {
    if (enable) {
        this.sendLang(lang, *args)
    }
}