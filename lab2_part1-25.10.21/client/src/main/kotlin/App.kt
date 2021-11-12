import api.ContactsAPI
import commands.*
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import plugins.errln
import java.net.ConnectException

suspend fun main(args: Array<String>) {
    val parser = ArgParser("server_params")
    val apiUrl by parser.argument(ArgType.String, "api", "url to api")
    parser.parse(args)
    ContactsAPI.BASE = apiUrl
    println("""
        Enter the number of action and press [Enter]. Then follow instructions.
        
    """.trimIndent())
    val commands = listOf(ShowContactsCmd(), SearchContactsCmd(), AddContactCmd(), ExitCmd())
    ManCmd()(commands)
    while (true) {
        print("> ")
        val input = readLine().orEmpty()
        if (input.isBlank()) continue
        try {
            commands[input.toInt().also { if (it > commands.count()) throw NumberFormatException() }]()
        } catch (e: Exception) {
            errln(
                when(e) {
                    is NumberFormatException -> "There are no command with number = $input. Try again."
                    is ConnectException -> "Problems with network. Check your internet connection or try it."
                    else -> "Unexpected exception. Show this to support: ${e.stackTraceToString()}"
                }
            )
        }
    }
}