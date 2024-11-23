package host.carbon.plugin.spigot

import host.carbon.common.KtorManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.SimpleCommandMap
import org.bukkit.command.TabExecutor
import org.bukkit.plugin.java.JavaPlugin


class CarbonPlugin : JavaPlugin() {
    override fun onEnable() {
        KtorManager(CarbonPluginAPI()).startServer()
    }

    override fun onDisable() {}
}