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
import java.nio.file.Files
import java.nio.file.Path

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

        val configFile = dataDirectory.resolve("config.yml")
        if (Files.notExists(configFile)) {
            javaClass.classLoader.getResourceAsStream("config.yml")?.use {
                Files.copy(it, configFile)
            }
        }

        val yaml = Yaml()
        val config: Map<String, Any> = Files.newInputStream(configFile).use { inputStream ->
            yaml.load(inputStream)
        }

        val port = config["port"] as Int

        ktorManager = KtorManager(CarbonPluginAPI(server), port)
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