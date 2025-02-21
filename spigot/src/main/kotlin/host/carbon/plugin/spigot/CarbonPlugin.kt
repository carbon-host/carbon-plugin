package host.carbon.plugin.spigot

import host.carbon.common.CarbonAPI
import host.carbon.common.KtorManager
import host.carbon.common.types.AnalyticInfo
import host.carbon.common.types.ServerInfo
import host.carbon.common.types.ServerResourceInfo
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

class CarbonPlugin : JavaPlugin() {
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
            port = config.getInt("port")
            carbonKey = config.getString("carbon-key")!!
        } else {
            configFile.createNewFile()

            config.set("port", 25505)
            config.set("carbon-key", carbonKey)
            saveConfig()
        }

        carbonAPI = CarbonPluginAPI()

        ktorManager = KtorManager(carbonAPI, port, carbonKey)
        ktorManager.startServer()

        logger.info("Carbon API started on port $port")

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, Runnable {
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
        }, 0, 20)
    }

    override fun onDisable() {
        if (::ktorManager.isInitialized) {
            ktorManager.stopServer()
            logger.info("Carbon API stopped")
        }
    }
}