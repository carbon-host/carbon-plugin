package host.carbon.common.types.performance

data class TPSInfo(
    val fiveSeconds: Double,
    val tenSeconds: Double,
    val oneMinute: Double,
    val fiveMinutes: Double,
    val fifteenMinutes: Double
)

data class TickDurations(
    val min: Double,
    val median: Double,
    val p95: Double,
    val max: Double
)

data class CPUUsage(
    val system: Double,
    val process: Double
)

data class MemoryUsage(
    val used: Double,
    val total: Double
)

data class NetworkStats(
    val bytesPerSecond: Double,
    val packetsPerSecond: Int
)

data class PerformanceInfo(
    val tps: TPSInfo,
    val tickDurations: TickDurations,
    val cpuUsage: CPUUsage,
    val memoryUsage: MemoryUsage,
    val networkRx: NetworkStats,
    val networkTx: NetworkStats,
    val diskUsed: Double,
    val diskTotal: Double
)