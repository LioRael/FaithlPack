package com.faithl.pack.common.core

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

/**
 * @author Leosouthey
 * @since 2022/4/30-20:01
 **/
class OpeningPack(val player: Player, val packData: PackData, var inventory: Inventory, var page: Int = 1)