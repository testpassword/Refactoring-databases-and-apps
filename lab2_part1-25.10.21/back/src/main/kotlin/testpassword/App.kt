package testpassword

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import testpassword.plugins.configureDatabase
import testpassword.plugins.configureHTTP
import testpassword.plugins.configureModules
import testpassword.plugins.configureRouting

fun main(args: Array<String>) {
    val parser = ArgParser("server_params")
    val port by parser.option(ArgType.Int, "port", "p", "service port").default(80)
    val jdbcUrl by parser.argument(ArgType.String, "jdbc", "jdbc url to database")
    parser.parse(args)
    embeddedServer(Netty, port, "0.0.0.0") {
        configureDatabase(jdbcUrl)
        configureRouting()
        configureHTTP()
        configureModules()
    }.start(wait = true)
}