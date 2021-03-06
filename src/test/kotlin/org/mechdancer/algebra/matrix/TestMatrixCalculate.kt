package org.mechdancer.algebra.matrix

import org.junit.Assert
import org.junit.Test
import org.mechdancer.algebra.DOUBLE_PRECISION
import org.mechdancer.algebra.core.rowView
import org.mechdancer.algebra.function.matrix.*
import org.mechdancer.algebra.implement.matrix.builder.I
import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.algebra.implement.matrix.special.HilbertMatrix
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

class TestMatrixCalculate {
	@Test
	fun problem1() {
		problem(
			name = "1-1",
			result = matrix {
				row(0, 0, 1)
				row(0, 1, 0)
				row(1, 0, 0)
			}) {
			val b = matrix {
				row(2, 0, 2)
				row(0, 4, 0)
				row(2, 0, 2)
			}
			val a = b * (b - 2 * I[3]).inverse()
			(a - I[3]).inverse()
		}
	}

	@Test
	fun problem2() {
		problem(
			name = "1-3",
			result = matrix {
				row(-2, -1)
				row(+2, +0)
			}) {
			val a = matrix {
				row(+1, -1)
				row(+2, +3)
			}
			(a power 2) - 3 * a + 2 * I[2]
		}
	}

	@Test
	fun problem3() {
		problem(
			name = "3-1",
			result = matrix {
				row(-2, +1)
				row(10, -4)
				row(-10, 4)
			}) {
			val a = matrix {
				row(1, 2, 3)
				row(2, 2, 1)
				row(3, 4, 3)
			}
			val b = matrix {
				row(2, 1)
				row(5, 3)
			}
			val c = matrix {
				row(1, 3)
				row(2, 0)
				row(3, 1)
			}
			a.inverse() * c * b.inverse()
		}
	}

	@Test
	fun problem4() {
		problem(
			name = "3-2",
			result = matrix {
				row(+2, 0, 1)
				row(+0, 3, 0)
				row(-1, 0, 2)
			}) {
			val a = matrix {
				row(+1, 0, 1)
				row(+0, 2, 0)
				row(-1, 0, 1)
			}
			(I[3] - (a power 2)) * (I[3] - a).inverse()
		}
	}

	@Test
	fun problem5() {
		problem(
			name = "3-3",
			result = matrix {
				row(+1, 0, 0, 0)
				row(-2, 1, 0, 0)
				row(1, -2, 1, 0)
				row(0, 1, -2, 1)
			}) {
			val b = matrix {
				row(1, -1, 0, 0)
				row(0, 1, -1, 0)
				row(0, 0, 1, -1)
				row(0, 0, 0, +1)
			}
			val c = matrix {
				row(2, 1, 3, 4)
				row(0, 2, 1, 3)
				row(0, 0, 2, 1)
				row(0, 0, 0, 2)
			}

			val ct = c.transpose()
			val xt = (I[4] - c.inverse() * b).transpose()

			I[4] * ct.inverse() * xt.inverse()
		}
	}

	@Test
	fun problem6() {
		problem(
			name = "3-4",
			result = matrix {
				row(1, -8, -3)
				row(10, 1, +4)
				row(2, -5, -2)
			}) {
			val a = matrix {
				row(1, 2, 1)
				row(2, 1, 1)
				row(1, 1, 2)
			}
			val b = matrix {
				row(-1, 1, 0)
				row(+1, 3, 1)
				row(-1, 0, 1)
			}
			(a + b power 2) - (a * a + 2 * a * b + b * b)
		}
	}

	@Test
	fun problem7() {
		problem(
			name = "3-5",
			result = matrix {
				row(1, 2, 5)
				row(0, 1, 2)
				row(0, 0, 1)
			}) {
			val a = matrix {
				row(1, 0, 0)
				row(1, 1, 0)
				row(1, 1, 1)
			}
			val b = matrix {
				row(0, 1, 1)
				row(1, 0, 1)
				row(1, 1, 0)
			}
			(a - b).inverse() power 2
		}
	}

	@Test
	fun problem8() {
		val sqrt2 = sqrt(2.0)
		val eigenvalue = setOf(2 - sqrt2, 2.0, 2 + sqrt2).also(::println)
		val matrix = matrix {
			row(2, -1, 0)
			row(-1, 2, -1)
			row(0, -1, 2)
		}
		matrix.eigen(1E-14)
			.also {
				it.forEach { pair ->
					val (l, v) = pair
					println("$l: ${v.rowView()}")
				}
			}
			.map { it.first }
			.reversed()
			.zip(eigenvalue) { a, b -> abs(a - b) }
			.all { it.also(::println) < 1E-6 }
			.let(Assert::assertTrue)
	}

	@Test
	fun testMatrixNorm() {
		val a = HilbertMatrix[3, 5]
		Assert.assertEquals(1.833333333333333, a.norm(1), DOUBLE_PRECISION)
		Assert.assertEquals(1.480485000746432, a.norm(2), DOUBLE_PRECISION)
		Assert.assertEquals(2.283333333333333, a.norm(-1), DOUBLE_PRECISION)
	}

	@Test
	fun testEfficiency() {
		fun test(seconds: Number, block: () -> Unit) {
			measureTimeMillis(block)
				.div(1000.0)
				.also(::println)
				.also { Assert.assertTrue(it < seconds.toDouble()) }
		}

		HilbertMatrix[40]
			.also { test(.1) { it.inverse() } }
			.also { test(.1) { it.transpose() * it } }
			.also { test(30) { println(it.norm()) } }
	}
}
