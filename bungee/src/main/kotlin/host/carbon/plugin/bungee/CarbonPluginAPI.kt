package host.carbon.plugin.bungee

import host.carbon.common.CarbonAPI
import host.carbon.common.types.players.PlayerInfo
import net.md_5.bungee.api.ProxyServer
import java.lang.management.ManagementFactory

class CarbonPluginAPI : CarbonAPI() {
    override fun getMaxPlayers(): Int {
        return ProxyServer.getInstance().config.playerLimit
    }

    override fun getOnlinePlayerCount(): Int {
        return ProxyServer.getInstance().onlineCount
    }

    override fun getOnlinePlayers(): List<PlayerInfo> {
        return ProxyServer.getInstance().players
            .map { player ->
                PlayerInfo(
                    player.name,
                    player.uniqueId.toString(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    player.ping,
                    null
                )
            }
    }

    override fun getCommands(query: String?): List<String> {
        return ProxyServer.getInstance().pluginManager.commands.map { it.key }
    }

    override fun getTPS(): Double? {
        return null
    }

    override fun getMSPT(): Double? {
        return null
    }

    override fun getMemoryUsage(): Long {
        return ManagementFactory.getMemoryMXBean().heapMemoryUsage.used
    }

    override fun getTotalMemory(): Long {
        return ManagementFactory.getMemoryMXBean().heapMemoryUsage.max
    }

    override fun getCPUUsage(): Double {
        return (ManagementFactory.getPlatformMXBean(com.sun.management.OperatingSystemMXBean::class.java).processCpuLoad * 100)
    }

    override fun getCPUCores(): Double {
        return Runtime.getRuntime().availableProcessors().toDouble()
    }
}