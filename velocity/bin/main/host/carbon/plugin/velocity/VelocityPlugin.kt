package host.carbon.plugin.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import org.slf4j.Logger

@Plugin(
    id = "carbon-plugin",
    name = "CarbonPlugin",
    version = "1.0.0",
    description = "The Plugin providing information to Carbon.host"
)
class VelocityPlugin @Inject constructor(val server: ProxyServer, val logger: Logger) {

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent?) {
    }


}