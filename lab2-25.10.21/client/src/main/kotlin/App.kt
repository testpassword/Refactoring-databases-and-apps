import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

suspend fun main() =
    HttpClient(CIO).use { println(it.get<HttpResponse>("https://ktor.io/").status) }