/*
 * SlotLimiter - A simple plugin for Bukkit servers not relying on a hard cap.
 * Copyright (C) NotMyFault 2020
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package de.n0tmyfaultog.slotlimiter

import me.lucko.helper.Events
import me.lucko.helper.plugin.ExtendedJavaPlugin
import me.lucko.helper.text3.Text
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import me.lucko.helper.utils.Players
import java.util.UUID
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.query.QueryOptions
import java.lang.InterruptedException
import java.util.concurrent.ExecutionException

class SlotLimiterPlugin : ExtendedJavaPlugin() {
    private var onlinePlayerCap = 0
    private var serverIsFullMessage: String? = null
    override fun load() {
        saveDefaultConfig()
        onlinePlayerCap = this.config.getInt("online_player_cap")
        serverIsFullMessage = Text.colorize(this.config.getString("server_is_full"))
    }

    override fun enable() {
        Events.subscribe(AsyncPlayerPreLoginEvent::class.java).handler { e: AsyncPlayerPreLoginEvent ->
            if (Players.all().size >= onlinePlayerCap && !hasPermission(e.uniqueId, e.name)) {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, serverIsFullMessage!!)
            }
        }.bindWith(this)
    }

    override fun disable() {
        super.disable()
    }

    private fun hasPermission(user: UUID, username: String): Boolean {
        try {
            return LuckPermsProvider.get().userManager.loadUser(user, username).get().cachedData.getPermissionData(
                QueryOptions.defaultContextualOptions()
            ).checkPermission("slotlimiter.bypass").asBoolean()
        } catch (exception: InterruptedException) {
            exception.printStackTrace()
        } catch (exception: ExecutionException) {
            exception.printStackTrace()
        }
        return true
    }
}