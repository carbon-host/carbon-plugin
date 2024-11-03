package host.carbon.common.types

data class PlayersInfo(
    val players: List<String>,
    val countInfo: PlayerCountInfo
)

data class PlayerCountInfo(
    val onlinePlayers: Int,
    val maxPlayers: Int,
)