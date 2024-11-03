package host.carbon.plugin.spigot

import host.carbon.common.CarbonAPI
import org.bukkit.Bukkit

class CarbonPluginAPI : CarbonAPI() {
    override fun getMaxPlayers(): Int {
        return Bukkit.getMaxPlayers()
    }

    override fun getOnlinePlayerCount(): Int {
        return Bukkit.getOnlinePlayers().size
    }

    override fun getOnlinePlayers(): List<String> {
        return Bukkit.getOnlinePlayers().map { it.name }
    }


    override fun getTPS(): Double {
        return 20.0
    }

}