package host.carbon.plugin.spigot

import host.carbon.common.KtorManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.SimpleCommandMap
import org.bukkit.command.TabExecutor
import org.bukkit.plugin.java.JavaPlugin


class CarbonPlugin : JavaPlugin() {
    private lateinit var ktorManager: KtorManager

    override fun onEnable() {
        saveConfig()

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