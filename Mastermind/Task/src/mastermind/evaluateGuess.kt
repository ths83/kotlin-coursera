package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    if (secret == guess) return Evaluation(4, 0)

    return getEvaluation(toCharIndexMap(guess), toCharIndexMap(secret))
}

fun toCharIndexMap(string: String): Map<Char, List<Int>?> {
    val charToIndexList = mutableMapOf<Char, List<Int>?>()
    string.withIndex().forEach { (index, c) ->
        if (charToIndexList[c] == null) {
            charToIndexList[c] = mutableListOf(index)
        } else {
            val list = charToIndexList[c]?.toMutableList()
            list?.add(index)
            charToIndexList[c] = list
        }
    }

    return charToIndexList
}

fun getEvaluation(map1: Map<Char, List<Int>?>, map2: Map<Char, List<Int>?>): Evaluation {
    var rightPosition = 0
    var wrongPosition = 0

    map1.forEach { m ->
        if (map2.containsKey(m.key)) {
            val evaluation = getRightAndWrongPosition(m.value, map2.getValue(m.key))
            rightPosition += evaluation.rightPosition
            wrongPosition += evaluation.wrongPosition
        }
    }

    return Evaluation(rightPosition, wrongPosition)
}

fun getRightAndWrongPosition(guess: List<Int>?, secret: List<Int>?): Evaluation {
    var rightPosition = 0
    var wrongPosition = 0
    val smallest: List<Int>?
    val biggest: List<Int>?

    if (secret!!.size > guess!!.size) {
        smallest = guess
        biggest = secret
    } else {
        smallest = secret
        biggest = guess
    }

    smallest.forEach { c ->
        if (biggest.contains(c)) rightPosition++
        else wrongPosition++
    }

    return Evaluation(rightPosition, wrongPosition)
}
