package host.carbon.plugin.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import host.carbon.common.KtorManager
import org.slf4j.Logger
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

@Plugin(
    id = "carbon-plugin",
    name = "CarbonPlugin",
    version = "1.0.0",
    description = "The Plugin providing information to Carbon.host"
)
class VelocityPlugin @Inject constructor(
    val server: ProxyServer,
    val logger: Logger,
    @DataDirectory val dataDirectory: Path
) {
    private lateinit var ktorManager: KtorManager

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
                Yaml().dump(
                    mapOf(
                        "port" to port,
                        "carbon-key" to carbonKey
                    ), outputStream.writer()
                )
            }
        }

        ktorManager = KtorManager(CarbonPluginAPI(server), port, carbonKey)
        ktorManager.startServer()

        logger.info("Carbon API started on port $port")
    }

    @Subscribe
    fun onProxyShutdown(event: ProxyShutdownEvent?) {
        if (::ktorManager.isInitialized) {
            ktorManager.stopServer()
            logger.info("Carbon API stopped")
        }
    }
}