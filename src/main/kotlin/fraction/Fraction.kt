package fraction

import kotlin.math.abs

class Fraction(
		numerator: Int,
		denominator: Int) : Number(), Comparable<Fraction> {

	val isNegative = numerator < 0

	val denominator: Int

	val numerator: Int

	init {
		if (denominator == 0) throw IllegalStateException("分母不能为零")
		else {
			val factor = commonFactor(numerator, denominator)
			val n = numerator / factor
			val d = denominator / factor
			if (denominator < 0) {
				this.numerator = -n
				this.denominator = -d
			} else {
				this.numerator = n
				this.denominator = d
			}
		}
	}

	private fun simplify(a: Fraction): Fraction {
		val factor = commonFactor(a.numerator, a.denominator)
		val n = a.numerator / factor
		val d = a.denominator / factor
		return Fraction(n, d)
	}


	operator fun plus(other: Fraction): Fraction {
		val dFactor = commonFactor(denominator, other.denominator)
		val a = other.denominator / dFactor
		val b = denominator / dFactor
		val sum = Fraction(numerator * a + other.numerator * b,
				denominator * other.denominator / dFactor)
		return simplify(sum)
	}

	operator fun plus(other: Int): Fraction {
		return Fraction(numerator + other * denominator, denominator)
	}

	operator fun minus(other: Fraction): Fraction = this + (other * -1)

	operator fun minus(other: Int): Fraction {
		return Fraction(numerator - other * denominator, denominator)
	}

	operator fun times(other: Fraction): Fraction =
			simplify(Fraction(numerator * other.numerator,
					denominator * other.denominator))

	operator fun times(k: Int): Fraction = simplify(Fraction(numerator * k, denominator))

	operator fun div(other: Fraction): Fraction =
			simplify(Fraction(numerator * other.denominator
					, denominator * other.numerator))


	operator fun div(other: Int): Fraction =
			if (other == 0) throw IllegalStateException("除数不能为零")
			else simplify(Fraction(numerator, denominator * other))


	fun reciprocal(): Fraction =
			if (numerator == 0) throw IllegalStateException("求倒数分子不能为零")
			else Fraction(denominator, numerator)


	override fun toInt(): Int = numerator / denominator

	override fun toDouble(): Double = numerator.toDouble() / denominator.toDouble()


	override fun toByte(): Byte = toDouble().toByte()

	override fun toChar(): Char = toDouble().toChar()

	override fun toFloat(): Float = numerator.toFloat() / denominator.toFloat()

	override fun toLong(): Long = toInt().toLong()

	override fun toShort(): Short = toInt().toShort()

	override fun compareTo(other: Fraction): Int = toDouble().let {
		val o = other.toDouble()
		when {
			it > o  -> 1
			it < o  -> -1
			it == o -> 0
			else    -> throw IllegalStateException("喵喵喵")
		}
	}

	fun toSimpleString(): String = "$numerator / $denominator"

	override fun toString(): String = buildString {
		val data = arrayOf(numerator, denominator)
		val maxDataLength = data.map { it.toString().length }.max()!!

		data.forEachIndexed { i, it ->
			var dL = abs(maxDataLength - it.toString().length)
			val parity = dL % 2 == 0
			dL /= 2
			val right: Int = dL
			val left: Int = if (parity) dL else dL + 1
			append(" ".repeat(left))
			append(" $it ")
			append(" ".repeat(right))
			if (i != 1) {
				appendln()
				append(" ".repeat(left))
				append(" ")
				append("─".repeat(maxDataLength))
				append(" ")
				append(" ".repeat(right))
				appendln()
			}
		}
	}

	override fun equals(other: Any?): Boolean {
		if (other !is Fraction) return false
		return toDouble() == other.toDouble()
	}

	override fun hashCode(): Int {
		var result = isNegative.hashCode()
		result = 31 * result + denominator
		result = 31 * result + numerator
		return result
	}

	companion object {

		fun commonFactor(x: Int, y: Int): Int {
			var a = abs(x)
			var b = abs(y)
			var temp: Int
			while (a != 0) {
				temp = b
				b = a
				a = temp % a
			}
			return b
		}

	}
}