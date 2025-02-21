package host.carbon.common

import host.carbon.common.types.PaginatedResponse
import host.carbon.common.types.Pagination
import host.carbon.common.types.ServerInfo
import host.carbon.common.types.ServerResourceInfo
import host.carbon.common.types.players.PlayersInfo
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*

class KtorManager(private val carbonAPI: CarbonAPI, port: Int, private val carbonKey: String) {

    private val keyAuth = createRouteScopedPlugin("keyAuth") {
        onCall { call ->
            val authHeader = call.request.headers["Authorization"]
            val key = authHeader?.removePrefix("Bearer ")?.trim()

            if (key != carbonKey) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid or missing API key")
                return@onCall
            }
        }
    }

    private val server = embeddedServer(
        Netty,
        port = port,
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
//                install(keyAuth)

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

                // last 30 seconds by default
                get("/analytics") {
                    call.respond(carbonAPI.analytics)
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
    }

    fun startServer() {
        server.start(wait = false)
    }

    fun stopServer() {
        server.stop()
    }
}