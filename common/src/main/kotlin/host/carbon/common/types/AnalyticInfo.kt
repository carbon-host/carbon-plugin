package host.carbon.common.types

import java.util.Date

data class AnalyticInfo(
    val serverInfo: ServerInfo,
    val createdAt: Date,
)
