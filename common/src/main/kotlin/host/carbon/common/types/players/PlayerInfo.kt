package host.carbon.common.types.players

data class PlayerInfo(
    val name: String,
    val uuid: String,

    val firstPlayed: Long,
    val lastSeen: Long,
    val lastLogin: Long,

    val isOp: Boolean,
    val idleDuration: Long,
    val ping: Int,

    val location: PlayerLocation,

)

data class PlayerLocation(
    val x: Double,
    val y: Double,
    val z: Double,
    val world: String,
)