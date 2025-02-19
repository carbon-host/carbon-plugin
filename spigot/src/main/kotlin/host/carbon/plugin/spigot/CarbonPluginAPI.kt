package host.carbon.plugin.spigot

import host.carbon.common.CarbonAPI
import host.carbon.common.types.players.PlayerInfo
import host.carbon.common.types.players.PlayerLocation
import me.lucko.spark.api.SparkProvider
import me.lucko.spark.api.statistic.StatisticWindow
import org.bukkit.Bukkit
import java.lang.management.ManagementFactory

class CarbonPluginAPI : CarbonAPI() {
    val spark by lazy { SparkProvider.get() }

    override fun getMaxPlayers(): Int {
        return Bukkit.getMaxPlayers()
    }

    override fun getOnlinePlayerCount(): Int {
        return Bukkit.getOnlinePlayers().size
    }

    override fun getOnlinePlayers(): List<PlayerInfo> {
        return Bukkit.getOnlinePlayers()
            .map { player ->
                PlayerInfo(
                    player.name,
                    player.uniqueId.toString(),
                    player.firstPlayed,
                    player.lastSeen,
                    player.lastLogin,
                    player.isOp,
                    player.idleDuration.toMillis(),
                    player.ping,
                    PlayerLocation(
                        player.location.x,
                        player.location.y,
                        player.location.z,
                        player.location.world.name
                    )
                )
            }
    }

    override fun getCommands(query: String?): List<String> {
        return Bukkit.getServer().commandMap.knownCommands.map { it.key }.filter { it.startsWith(query ?: "", true) }
    }

    override fun getTPS(): Double? {
        return spark.tps()?.poll(StatisticWindow.TicksPerSecond.SECONDS_5)
    }

    override fun getMSPT(): Double? {
        return spark.mspt()?.poll(StatisticWindow.MillisPerTick.SECONDS_10)?.mean()
    }

    override fun getMemoryUsage(): Long {
        return ManagementFactory.getMemoryMXBean().heapMemoryUsage.used
    }

    override fun getTotalMemory(): Long {
        return ManagementFactory.getMemoryMXBean().heapMemoryUsage.max
    }

    override fun getCPUUsage(): Double {
        return spark.cpuProcess().poll(StatisticWindow.CpuUsage.SECONDS_10)
    }

    override fun getCPUCores(): Double {
        return Runtime.getRuntime().availableProcessors().toDouble()
    }
}