package host.carbon.plugin.bungee

import host.carbon.common.CarbonAPI
import host.carbon.common.KtorManager
import host.carbon.common.types.AnalyticInfo
import host.carbon.common.types.ServerInfo
import host.carbon.common.types.ServerResourceInfo
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

class CarbonBungeePlugin : Plugin() {
    private lateinit var ktorManager: KtorManager
    lateinit var carbonAPI: CarbonAPI

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

        carbonAPI = CarbonPluginAPI()

        ktorManager = KtorManager(carbonAPI, port, carbonKey)
        ktorManager.startServer()

        logger.info("Carbon API started on port $port")

        proxy.scheduler.schedule(this, {
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
        }, 1, TimeUnit.SECONDS)
    }

    override fun onDisable() {
        if (::ktorManager.isInitialized) {
            ktorManager.stopServer()
            logger.info("Carbon API stopped")
        }
    }
}