package commands

class ManCmd: Command<Iterable<Command<*>>?> {

    override val key = "Help"

    override fun toString() = "Writes help msg about other commands to standard stdout"

    override suspend fun invoke(args: Iterable<Command<*>>?) = args!!.forEachIndexed { i, cmd -> println("$i: ${cmd.key} - $cmd") }
}