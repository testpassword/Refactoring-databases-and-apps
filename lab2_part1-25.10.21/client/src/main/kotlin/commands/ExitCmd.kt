package commands

import kotlin.system.exitProcess

class ExitCmd: Command<Int> {

    override val key = "Exit"

    override fun toString() = "Exit from program"

    override suspend fun invoke(args: Int?) {
        println("""
            
            Goodbye!
        """.trimIndent())
        exitProcess(args ?: 0)
    }
}