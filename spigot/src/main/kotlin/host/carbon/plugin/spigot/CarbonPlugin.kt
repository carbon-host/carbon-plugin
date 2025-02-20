package host.carbon.plugin.spigot

import host.carbon.common.KtorManager
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

class CarbonPlugin : JavaPlugin() {
    private lateinit var ktorManager: KtorManager

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