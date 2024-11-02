package host.carbon.common

import host.carbon.common.types.ServerInfo
import io.ktor.serialization.gson.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class KtorManager(val carbonAPI: CarbonAPI) {

    fun startServer() {
        embeddedServer(
            Netty,
            port = 8080,
        ) {
            install(CORS)
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                }
            }

            routing {
                get("/") {
                    val info = ServerInfo(
                        isProxy = carbonAPI.getIsProxy(),
                        maxPlayers = carbonAPI.getMaxPlayers(),
                        onlinePlayers = carbonAPI.getOnlinePlayerCount(),
                        tps = carbonAPI.getTPS()
                    )

                    call.respond(info)
                }
            }
        }.start(wait = false)
    }

}