package com.github.mlgpenguin.multiplatformtemplate.logic

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class KtorManager {

    fun startServer(proxy: Boolean) {
        embeddedServer(
            Netty,
            port = 8080,
            module = if (proxy) Application::proxyModule else Application::nonProxyModule
        ).start(wait = false)
    }

}

fun Application.proxyModule() {
    routing {
        get("/") {
            call.respondText("Proxy")
        }
    }
}

fun Application.nonProxyModule() {
    routing {
        get("/") {
            call.respondText("Non proxy")
        }
    }
}