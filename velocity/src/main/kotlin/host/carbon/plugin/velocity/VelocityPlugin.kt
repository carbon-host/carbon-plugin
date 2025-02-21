package host.carbon.plugin.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import host.carbon.common.KtorManager
import host.carbon.common.types.AnalyticInfo
import host.carbon.common.types.ServerInfo
import host.carbon.common.types.ServerResourceInfo
import org.slf4j.Logger
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.concurrent.TimeUnit

@Plugin(
    id = "carbon-plugin",
    name = "CarbonPlugin",
    version = "1.0.0",
    description = "The Plugin providing information to Carbon.host"
)
class VelocityPlugin @Inject constructor(
    val server: ProxyServer, val logger: Logger, @DataDirectory val dataDirectory: Path
) {
    private lateinit var ktorManager: KtorManager
    lateinit var carbonAPI: CarbonPluginAPI

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent?) {
        Files.createDirectories(dataDirectory)

        var port = 25505
        var carbonKey = UUID.randomUUID().toString()

        val configFile = dataDirectory.resolve("config.yml").toFile()
        if (configFile.exists()) {
            Files.newInputStream(configFile.toPath()).use { inputStream ->
                val config: Map<String, Any> = Yaml().load(inputStream)
                port = config["port"] as Int
                carbonKey = config["carbon-key"] as String
            }
        } else {
            Files.newOutputStream(configFile.toPath()).use { outputStream ->
                val options = DumperOptions().apply {
                    defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
                    isPrettyFlow = true
                }
                Yaml(options).dump(
                    mapOf(
                        "port" to port, "carbon-key" to carbonKey
                    ), outputStream.writer()
                )
            }
        }

        carbonAPI = CarbonPluginAPI(server)

        ktorManager = KtorManager(carbonAPI, port, carbonKey)
        ktorManager.startServer()

        logger.info("Carbon API started on port $port")

        server.scheduler.buildTask(this, Runnable {
            carbonAPI.analytics.add(
                AnalyticInfo(
                    ServerInfo(
                        carbonAPI.getTPS(),
                        carbonAPI.getMSPT(),
                        ServerResourceInfo(
                            carbonAPI.getMemoryUsage(),
                            carbonAPI.getTotalMemory(),
                            carbonAPI.getCPUUsage(),
                            carbonAPI.getCPUCores()
                        ),
                        carbonAPI.getPlayerCountInfo(),
                    ), Date().time
                )
            )

            val thirtySecondsAgo = System.currentTimeMillis() - 30 * 1000
            carbonAPI.analytics.removeIf { it.createdAt < thirtySecondsAgo }
        }).repeat(1, TimeUnit.SECONDS).schedule()
    }

    @Subscribe
    fun onProxyShutdown(event: ProxyShutdownEvent?) {
        if (::ktorManager.isInitialized) {
            ktorManager.stopServer()
            logger.info("Carbon API stopped")
        }
    }
}