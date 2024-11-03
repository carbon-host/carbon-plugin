package host.carbon.common

abstract class CarbonAPI {
    abstract fun getMaxPlayers(): Int
    abstract fun getOnlinePlayerCount(): Int
    abstract fun getOnlinePlayers(): List<String>
    abstract fun getTPS(): Double?

    fun getOnlinePlayers(limit: Int, offset: Int): List<String> {
        val players = getOnlinePlayers()
        val fromIndex = offset.coerceAtLeast(0).coerceAtMost(players.size)
        val toIndex = (offset + limit).coerceIn(fromIndex, players.size)
        return players.subList(fromIndex, toIndex)
    }
}