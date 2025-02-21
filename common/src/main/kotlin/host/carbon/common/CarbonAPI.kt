package host.carbon.common

import host.carbon.common.types.AnalyticInfo
import host.carbon.common.types.ServerInfo
import host.carbon.common.types.players.PlayerCountInfo
import host.carbon.common.types.players.PlayerInfo

abstract class CarbonAPI {
    val analytics = mutableListOf<AnalyticInfo>()

    abstract fun getMaxPlayers(): Int
    abstract fun getOnlinePlayerCount(): Int
    abstract fun getOnlinePlayers(): List<PlayerInfo>

    abstract fun getCommands(query: String?): List<String>

    abstract fun getTPS(): Double?
    abstract fun getMSPT(): Double?

    abstract fun getMemoryUsage(): Long
    abstract fun getTotalMemory(): Long
    abstract fun getCPUUsage(): Double
    abstract fun getCPUCores(): Double

    fun getOnlinePlayers(limit: Int, offset: Int): List<PlayerInfo> {
        val players = getOnlinePlayers()
        val fromIndex = offset.coerceAtLeast(0).coerceAtMost(players.size)
        val toIndex = (offset + limit).coerceIn(fromIndex, players.size)
        return players.subList(fromIndex, toIndex)
    }

    fun getPlayerCountInfo(): PlayerCountInfo {
        return PlayerCountInfo(
            getOnlinePlayerCount(),
            getMaxPlayers()
        )
    }

    fun getCarbonVersion(): String {
        return "1.0.1"
    }
}