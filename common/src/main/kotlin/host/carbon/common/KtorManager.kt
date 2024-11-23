package host.carbon.common

import host.carbon.common.types.*
import host.carbon.common.types.players.PlayerCountInfo
import host.carbon.common.types.players.PlayersInfo
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
                    call.respondText("Hello World!")
                }

                route("/v1") {
                    get {
                        val info = ServerInfo(
                            carbonAPI.getTPS(),
                            carbonAPI.getMSPT(),
                            ServerResourceInfo(
                                carbonAPI.getMemoryUsage(),
                                carbonAPI.getTotalMemory(),
                                carbonAPI.getCPUUsage(),
                                carbonAPI.getCPUCores()
                            ),
                            carbonAPI.getPlayerCountInfo(),
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
                                    carbonAPI.getPlayerCountInfo()
                                )
                            )
                        )
                    }

                    // TODO: Implement pagination
                    get("/commands") {
                        val limit = call.request.queryParameters["limit"]?.toInt() ?: 250
                        val offset = call.request.queryParameters["offset"]?.toInt() ?: 0
                        val query = call.request.queryParameters["query"]

                        val commands = carbonAPI.getCommands(query)

                        call.respond(
                            PaginatedResponse(
                                Pagination(limit, offset),
                                commands
                            )
                        )
                    }
                }
            }
        }.start(wait = false)
    }

}