package host.carbon.plugin.bungee

import host.carbon.common.KtorManager
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.util.*

class CarbonBungeePlugin : Plugin() {
    private lateinit var ktorManager: KtorManager

    override fun onEnable() {
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }

        var port = 25505
        var carbonKey = UUID.randomUUID().toString()

        val configFile = File(dataFolder, "config.yml")
        if (configFile.exists()) {
            val config = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(configFile)
            port = config.getInt("port")
            carbonKey = config.getString("carbon-key")
        } else {
            configFile.createNewFile()
            val config = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(configFile)
            config.set("port", port)
            config.set("carbon-key", carbonKey)
            ConfigurationProvider.getProvider(YamlConfiguration::class.java).save(config, configFile)
        }

        ktorManager = KtorManager(CarbonPluginAPI(), port, carbonKey)
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