package testpassword.plugins

infix operator fun String.minus(removable: String) = this.replace(removable, "")

fun errln(message: Any?) = System.err.println(message)