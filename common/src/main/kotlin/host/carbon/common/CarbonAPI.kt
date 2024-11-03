package host.carbon.common

import host.carbon.common.types.players.PlayerCountInfo

abstract class CarbonAPI {
    abstract fun getMaxPlayers(): Int
    abstract fun getOnlinePlayerCount(): Int
    abstract fun getOnlinePlayers(): List<String>

    abstract fun getTPS(): Double?
    abstract fun getMSPT(): Double?

    abstract fun getMemoryUsage(): Long
    abstract fun getTotalMemory(): Long
    abstract fun getCPUUsage(): Double
    abstract fun getCPUCores(): Double

    fun getOnlinePlayers(limit: Int, offset: Int): List<String> {
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

}