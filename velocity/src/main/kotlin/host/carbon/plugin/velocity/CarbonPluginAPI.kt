package host.carbon.plugin.velocity

import com.velocitypowered.api.proxy.ProxyServer
import host.carbon.common.CarbonAPI
import host.carbon.common.types.players.PlayerInfo
import java.lang.management.ManagementFactory

class CarbonPluginAPI(private val server: ProxyServer) : CarbonAPI() {
    override fun getMaxPlayers(): Int {
        return server.configuration.showMaxPlayers
    }

    override fun getOnlinePlayerCount(): Int {
        return server.playerCount
    }

    override fun getOnlinePlayers(): List<PlayerInfo> {
        return server.allPlayers
            .map { player ->
                PlayerInfo(
                    player.username,
                    player.uniqueId.toString(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    player.ping.toInt(),
                    null
                )
            }
    }

    override fun getCommands(query: String?): List<String> {
        return server.commandManager.aliases.filter { query == null || it.startsWith(query, true) }
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