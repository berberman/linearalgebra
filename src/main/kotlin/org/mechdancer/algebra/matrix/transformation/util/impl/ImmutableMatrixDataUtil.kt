package org.mechdancer.algebra.matrix.transformation.util.impl


import org.mechdancer.algebra.matrix.MatrixData
import org.mechdancer.algebra.matrix.MatrixElement

object ImmutableMatrixDataUtil {

	private val MatrixData.column
		get() = first().size

	private val MatrixData.row
		get() = size


	fun MatrixData.removeRow(row: Int): MatrixData {
		checkRowRange(row)
		return List(this.row - 1) { r ->
			List(this.column) { c ->
				if (r < row)
					this[r][c]
				else this[r + 1][c]
			}
		}
	}

	fun MatrixData.removeColumn(column: Int): MatrixData {
		checkColumnRange(column)
		return List(this.row) { r ->
			List(this.column - 1) { c ->
				if (c < column)
					this[r][c]
				else this[r][c + 1]
			}
		}
	}

	fun MatrixData.replaceRow(row: Int, elements: List<Double>): MatrixData {
		if (elements.size != this.column)
			throw IllegalArgumentException("the number elements are not equal")
		checkRowRange(row)
		return List(this.row) { r ->
			List(this.column) { c ->
				if (r == row) elements[c]
				else this[r][c]
			}
		}
	}

	fun MatrixData.replaceColumn(column: Int, elements: List<Double>): MatrixData {
		if (elements.size != this.row)
			throw IllegalArgumentException("the number elements are not equal")
		checkColumnRange(column)
		return List(this.row) { r ->
			List(this.column) { c ->
				if (c == column) elements[r]
				else this[r][c]
			}
		}
	}

	fun splitRow(data: MatrixData, row: Int): MatrixElement {
		data.checkRowRange(row)
		return List(data.column) { c ->
			data[row][c]
		}
	}

	fun splitColumn(data: MatrixData, column: Int): MatrixElement {
		data.checkColumnRange(column)
		return List(data.row) { r ->
			data[r][column]
		}
	}

	private fun MatrixData.checkRowRange(row: Int) = if (row !in 0 until this.row)
		throw IllegalArgumentException("row number error") else Unit


	private fun MatrixData.checkColumnRange(column: Int) = if (column !in 0 until this.column)
		throw IllegalArgumentException("column number error") else Unit
}

