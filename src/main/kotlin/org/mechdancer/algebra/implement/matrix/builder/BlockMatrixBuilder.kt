package org.mechdancer.algebra.implement.matrix.builder

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.implement.matrix.ArrayMatrix
import org.mechdancer.algebra.implement.matrix.ListMatrix
import org.mechdancer.algebra.implement.matrix.builder.BuilderMode.Immutable
import org.mechdancer.algebra.implement.matrix.builder.BuilderMode.ValueMutable
import org.mechdancer.algebra.uniqueValue

/**
 * 分块阵构建工具
 */
class BlockMatrixBuilder {
	private val data = mutableListOf<Double>()
	private var column = -1

	fun row(vararg matrix: Matrix) {
		// 一组的都一样高
		val row = matrix
			.map { it.row }
			.uniqueValue()
			?: throw IllegalArgumentException("matrices not in same height")
		// 和之前的组一样宽
		matrix
			.sumBy { it.column }
			.let { if (column == -1) column = it else assert(column == it) }
		// 添加到列表
		for (r in 0 until row)
			for (m in matrix)
				data.addAll(m.row(r).toList())
	}

	internal fun build(mode: BuilderMode): Matrix =
		when (mode) {
			Immutable    -> ListMatrix(column, data)
			ValueMutable -> ArrayMatrix(column, data.toDoubleArray())
		}
}
