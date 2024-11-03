package host.carbon.common.types

data class ServerInfo(
    val maxPlayers: Int,
    val onlinePlayers: Int,
    val tps: Double?,
)