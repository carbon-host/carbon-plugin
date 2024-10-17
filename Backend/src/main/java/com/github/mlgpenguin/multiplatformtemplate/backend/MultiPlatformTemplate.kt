package com.github.mlgpenguin.multiplatformtemplate.backend

import com.github.mlgpenguin.multiplatformtemplate.logic.KtorManager
import org.bukkit.plugin.java.JavaPlugin

class MultiPlatformTemplate: JavaPlugin() {
    override fun onEnable() {
        KtorManager().startServer(false)
    }
    override fun onDisable() {}
}