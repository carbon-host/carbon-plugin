package host.carbon.common

interface CarbonAPI {
    fun getMaxPlayers(): Int
    fun getOnlinePlayerCount(): Int
    fun getOnlinePlayers(limit: Int, offset: Int): List<String>
    fun getIsProxy(): Boolean
    fun getTPS(): Double
}