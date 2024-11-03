package host.carbon.common.types

import host.carbon.common.types.players.PlayerCountInfo

data class ServerInfo(
    val tps: Double?,
    val mspt: Double?,
    val resources: ServerResourceInfo,
    val playerCountInfo: PlayerCountInfo,
)

data class ServerResourceInfo(
    val ramUsage: Long,
    val ramTotal: Long,
    val cpuUsage: Double,
    val cpuCores: Double,
)