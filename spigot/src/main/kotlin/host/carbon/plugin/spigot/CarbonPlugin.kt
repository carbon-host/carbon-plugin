package host.carbon.plugin.spigot

import host.carbon.common.KtorManager
import org.bukkit.plugin.java.JavaPlugin

class CarbonPlugin: JavaPlugin() {
    override fun onEnable() {
        KtorManager().startServer(false)
    }
    override fun onDisable() {}
}