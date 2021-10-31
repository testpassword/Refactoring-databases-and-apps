package commands

interface Command<T> {

    val key: String

    suspend operator fun invoke(args: T? = null)
}