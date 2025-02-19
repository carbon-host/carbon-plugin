package host.carbon.plugin.bungee

import host.carbon.common.KtorManager
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import java.io.File

class CarbonBungeePlugin : Plugin() {
    private lateinit var ktorManager: KtorManager

    override fun onEnable() {
        if (dataFolder.exists()) {
            dataFolder.mkdir()
        }

        val configFile = File(dataFolder, "config.yml")
        if (!configFile.exists()) {
            getResourceAsStream("config.yml")?.use { input ->
                configFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        val config = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(File(dataFolder, "config.yml"))

        val port = config.getInt("port")

        ktorManager = KtorManager(CarbonPluginAPI(), port)
        ktorManager.startServer()

        logger.info("Carbon API started on port $port")

    }

    override fun onDisable() {
        if (::ktorManager.isInitialized) {
            ktorManager.stopServer()
            logger.info("Carbon API stopped")
        }
    }
}