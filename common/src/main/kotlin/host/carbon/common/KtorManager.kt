package host.carbon.common

import host.carbon.common.types.*
import io.ktor.serialization.gson.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class KtorManager(private val carbonAPI: CarbonAPI) {

    fun startServer() {
        embeddedServer(
            Netty,
            port = 6,
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
                        carbonAPI.getMaxPlayers(),
                        carbonAPI.getOnlinePlayerCount(),
                        carbonAPI.getTPS()
                    )

                    call.respond(info)
                }

                get("/players") {
                    val limit = call.request.queryParameters["limit"]?.toInt() ?: 25
                    val offset = call.request.queryParameters["offset"]?.toInt() ?: 0

                    val players = carbonAPI.getOnlinePlayers(limit.coerceAtMost(100), offset)

                    call.respond(
                        PaginatedResponse(
                            Pagination(limit, offset),
                            PlayersInfo(
                                players,
                                PlayerCountInfo(
                                    carbonAPI.getOnlinePlayerCount(),
                                    carbonAPI.getMaxPlayers(),
                                )
                            )
                        )
                    )
                }
            }
        }.start(wait = false)
    }

}