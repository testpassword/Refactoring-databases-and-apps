import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

class RestClient {
    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }
    private val serverPath = "http://localhost:8080/api"
    var bearer = ""

    fun get(path: String, param: Pair<String, Any>? = null): String =
        runBlocking {
            client.get("$serverPath$path") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $bearer")
                }
                param?.let { parameter(param.first, param.second) }
            }
        }

    fun post(path: String, payload: Map<String, Any>): String =
        runBlocking {
            client.post("$serverPath$path") {
                contentType(ContentType.Application.Json)
                body = payload
            }
        }
}