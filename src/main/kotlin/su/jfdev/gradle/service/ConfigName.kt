package su.jfdev.gradle.service

fun makeConfigName(sources: String, scope: String) = when (sources) {
    "main" -> scope
    else   -> camel(sources, scope)
}

fun camel(vararg words: String): String {
    if(words.isEmpty()) return ""
    val first = words.first().decapitalize()
    val other = words.drop(1)
    return buildString {
        append(first)
        for (next in other)
            append(next.capitalize())
    }
}