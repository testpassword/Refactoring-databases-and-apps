package plugins

infix operator fun String.minus(removable: String) = this.replace(removable, "")

fun errln(message: Any?) = System.err.println(message)

fun readUntilMatchesPattern(requiredField: String = "", pattern: Regex = Regex("^(?!\\s*$).+")): String =
    readLine().orEmpty().also {
        if (pattern.matches(it).not()) {
            if (requiredField.isNotBlank()) errln("$requiredField should match pattern $pattern")
            readUntilMatchesPattern(requiredField, pattern)
        }
    }