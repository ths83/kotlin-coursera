package nicestring

fun String.isNice(): Boolean {
    var result = 0
    if (this.containsAtLeastOneDoubleLetter()) result++
    if (!this.hasSubstring()) result++
    if (this.hasVowels()) result++

    return result >= 2
}

fun String.hasSubstring(): Boolean {
    return contains("bu") || contains("ba") || contains("be")
}

fun String.hasVowels(): Boolean {
    var vowelsNumber = 0
    val vowels = listOf('a', 'e', 'i', 'o', 'u')
    this.forEach {
        if (vowels.contains(it)) {
            vowelsNumber++
        }
    }
    return vowelsNumber >= 3
}

fun String.containsAtLeastOneDoubleLetter(): Boolean {
    if (this.isNotBlank()) {
        val stringWithoutFirstChar = this.removeRange(0, 1)
        val zip = this.zip(stringWithoutFirstChar)
        return zip.any { it.first == it.second }
    }

    return false
}