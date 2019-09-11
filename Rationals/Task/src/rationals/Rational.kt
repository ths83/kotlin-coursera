package rationals

import java.math.BigInteger


fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}

fun String.toRational(): Rational {
    val splitedRational = this.split('/')
    if (splitedRational.size == 1) return Rational(this.toBigInteger(), 1.toBigInteger())
    var numerator = splitedRational[0].toBigInteger()
    var denominator = splitedRational[1].toBigInteger()
    if (denominator.signum() == -1) {
        numerator = numerator.negate()
        denominator = denominator.negate()
    }
    val gcd = numerator.gcd(denominator)
    return Rational(numerator / gcd, denominator / gcd)
}

data class Rational(val numerator: BigInteger, val denominator: BigInteger) : Comparable<Rational> {
    override fun compareTo(other: Rational): Int {
        return when {
            this.numerator.toDouble() / this.denominator.toDouble() == other.numerator.toDouble() / other.denominator.toDouble() -> 0
            this.numerator.toDouble() / this.denominator.toDouble() > other.numerator.toDouble() / other.denominator.toDouble() -> 1
            else -> -1
        }
    }

    override fun toString(): String {
        if (this.numerator.remainder(this.denominator) == BigInteger.ZERO) return (this.numerator / this.denominator).toString()
        val gcd = this.numerator.gcd(this.denominator)
        return (this.numerator / gcd).toString() + "/" + (this.denominator / gcd).toString()
    }
}

operator fun Rational.plus(other: Rational) =
        Rational((this.numerator * other.denominator) + (other.numerator * this.denominator), (this.denominator * other.denominator))

operator fun Rational.minus(other: Rational) =
        Rational((this.numerator * other.denominator) - (other.numerator * this.denominator), (this.denominator * other.denominator))


operator fun Rational.times(other: Rational) =
        Rational(this.numerator * other.numerator, this.denominator * other.denominator)

operator fun Rational.div(other: Rational) = this.times(Rational(other.denominator, other.numerator))

operator fun Rational.unaryMinus() = Rational(-numerator, denominator)

operator fun Rational.rangeTo(that: RationalRange): Boolean = RationalRange(that.start, that.endInclusive).contains(this)

class RationalRange(override val start: Rational, override val endInclusive: Rational) : ClosedRange<Rational>

infix fun Long.divBy(i: Long): Rational {
    if (i == 0L) throw IllegalAccessException()
    val gcd = this.toBigInteger().gcd(i.toBigInteger())
    return Rational(this.toBigInteger() / gcd, i.toBigInteger() / gcd)
}

infix fun BigInteger.divBy(i: BigInteger): Rational {
    if (i == BigInteger.ZERO) throw IllegalAccessException()
    val gcd = this.gcd(i)
    return Rational(this / gcd, i / gcd)
}

infix fun Int.divBy(i: Int): Rational {
    if (i == 0) throw IllegalAccessException()
    val gcd = this.toBigInteger().gcd(i.toBigInteger())
    return Rational(this.toBigInteger() / gcd, i.toBigInteger() / gcd)
}

