import cn.berberman.algebra.equation.builder.linearEquation
import cn.berberman.algebra.equation.solvers.CommonSolver
import cn.berberman.algebra.matrix.builder.Row
import cn.berberman.algebra.matrix.builder.matrix
import org.junit.Assert
import org.junit.Test
import cn.berberman.algebra.vector.vectorOf

class TestEquation {
	@Test
	fun testCramerSolve() {

		/*
		2x+3y-5z=3
		x-2y+z=0
		3x+y+3z=7
		 */
		val equation = linearEquation {
			coefficient = matrix {
				Row[2.0, 3.0, -5.0]
				Row[1.0, -2.0, 1.0]
				Row[3.0, 1.0, 3.0]
			}

			constant = vectorOf(3.0, .0, 7.0)

		}
		Assert.assertEquals(vectorOf(10.0 / 7.0, 1.0, 4.0 / 7.0), equation.solve(CommonSolver))
	}

}