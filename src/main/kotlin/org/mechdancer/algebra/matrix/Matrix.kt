package org.mechdancer.algebra.matrix

import org.mechdancer.algebra.dimensionArgumentError
import org.mechdancer.algebra.matrix.determinant.Determinant
import org.mechdancer.algebra.matrix.transformation.ElementaryTransformation
import org.mechdancer.algebra.vector.Vector

/** 矩阵 */
interface Matrix {
	/** 维数（方阵才有维数） */
	val dimension: Int

	/** 数据 */
	val data: MatrixData

	/** 行数 */
	val row: Int

	/** 列数 */
	val column: Int

	/** 是否方阵 */
	val isSquare: Boolean

	/** @return 秩 */
	val rank: Int

	operator fun get(row: Int, column: Int): Double

	operator fun plus(other: Matrix): Matrix

	operator fun minus(other: Matrix): Matrix

	operator fun times(k: Double): Matrix

	operator fun times(other: Matrix): Matrix

	operator fun times(vector: Vector): Vector

	operator fun invoke(other: Matrix): Matrix

	operator fun invoke(vector: Vector): Vector

	operator fun div(other: Matrix): Matrix

	operator fun div(k: Double): Matrix

	infix fun pow(n: Int): Matrix

	fun toDeterminant(): Determinant

	fun det(): Double

	fun elementaryTransformation(block: ElementaryTransformation.() -> Unit): Matrix

	fun companion(): Matrix

	fun transpose(): Matrix

	fun rowEchelon(): Matrix

	fun withUnit(): Matrix

	fun inverseByCompanion(): Matrix

	fun inverseByRowEchelon(): Matrix

	fun clone(): Matrix

	companion object {
		fun zeroOf(row: Int, column: Int): Matrix {
			if (row <= 0 || column <= 0) throw IllegalArgumentException("matrix data error")
			return List(row) { _ -> List(column) { .0 } }.toMatrix()
		}

		fun unitOf(dimension: Int): Matrix {
			if (dimension <= 0) dimensionArgumentError()
			return List(dimension) { r ->
				List(dimension) { c ->
					if (c == r) 1.0 else .0
				}
			}.toMatrix()
		}
	}
}
