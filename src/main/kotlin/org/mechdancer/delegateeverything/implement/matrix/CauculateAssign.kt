package org.mechdancer.delegateeverything.implement.matrix

import org.mechdancer.delegateeverything.core.Matrix
import org.mechdancer.delegateeverything.core.MutableMatrix
import org.mechdancer.delegateeverything.core.ValueMutableMatrix

//对矩阵各项操作
private fun ValueMutableMatrix.forEach(block: (Double) -> Double) =
	(this as? ArrayMatrix)?.array
		?.let { it.forEachIndexed { i, value -> it[i] = block(value) } }
		?: run {
			for (r in 0 until row)
				for (c in 0 until column)
					this[r, c] = block(this[r, c])
		}

operator fun ValueMutableMatrix.timesAssign(k: Number) = forEach { it * k.toDouble() }
operator fun ValueMutableMatrix.divAssign(k: Number) = forEach { it / k.toDouble() }

private fun differentSizeException(a: Matrix, b: Matrix) =
	IllegalArgumentException("operate two matrix of different size (${a.row}*${a.column} and ${b.row}*${b.column})")

//对两个矩阵对应项操作
private fun ValueMutableMatrix.zip(other: Matrix, block: (Double, Double) -> Double) =
	takeIf { row == other.row && column == other.column }
		?.run {
			for (r in 0 until row)
				for (c in 0 until column)
					this[r, c] = block(this[r, c], other[r, c])
		}
		?: throw differentSizeException(this, other)

operator fun ValueMutableMatrix.plusAssign(other: Matrix) = zip(other) { a, b -> a + b }
operator fun ValueMutableMatrix.minusAssign(other: Matrix) = zip(other) { a, b -> a - b }

/**
 * 原地转置
 * 只适用于方阵
 */
fun ValueMutableMatrix.transposeAssign() {
	assert(row == column)
	for (r in 0 until row)
		for (c in 0 until r) {
			val temp = get(r, c)
			set(r, c, get(c, r))
			set(c, r, temp)
		}
}

/**
 * 原地获取余子式
 */
fun MutableMatrix.getCofactorAssign(r: Int, c: Int) =
	apply {
		removeRow(r)
		removeColumn(c)
	}

/**
 * 原地通过行初等变换变为行阶梯型阵
 */
fun ValueMutableMatrix.rowEchelonAssignWith(other: ValueMutableMatrix?) =
	apply {
		//固定行数
		var fixed = 0
		//按列化简
		for (c in 0 until column) {
			//在此列所有未固定的行中
			(fixed until row)
				//找到第一个非零元素
				.firstOrNull { r -> get(r, c) != .0 }
				//将其所在行交换到未固定的首行
				?.let {
					exchangeRow(it, fixed)
					other?.exchangeRow(it, fixed)
				}
			//全为零则直接查找下一列
				?: continue

			//取出这一行的首元
			val head = get(fixed, c)
			//用首元将此列未固定的其他元素化为零
			for (r in fixed + 1 until row) {
				val k = -get(r, c) / head
				plusToRow(k, fixed, r)
				other?.plusToRow(k, fixed, r)
				//强保证化为零有效
				set(r, c, .0)
			}

			//固定行数加一
			++fixed
		}
	}

/**
 * 原地通过行初等变换变为最简行阶梯型阵
 */
fun ValueMutableMatrix.simplifyAssignWith(other: ValueMutableMatrix?) =
	rowEchelonAssignWith(other)
		.apply {
			var c = column
			//清零上三角区
			for (r in (0 until row).reversed()) {
				//找到本行最后一个非零元素
				var tail: Double
				do {
					tail = get(r, --c)
				} while (c >= 0 && tail == .0)

				//有全零行，不再继续化简
				if (c < 0) break

				//用首元将此列未固定的其他元素化为零
				for (t in (0 until r)) {
					val k = -get(t, c) / tail
					plusToRow(k, r, t)
					other?.plusToRow(k, r, t)
					//强保证化为零有效
					set(t, c, .0)
				}
			}

			c = 0
			//主对角线归一化
			for (r in 0 until row)
				while (c < column) {
					val temp = get(r, c++)
					if (temp != .0) {
						timesRow(r, 1 / temp)
						other?.timesRow(r, 1 / temp)
						break
					}
				}
		}

/**
 * 原地通过行初等变换变为行阶梯型阵
 */
fun ValueMutableMatrix.rowEchelonAssign() =
	rowEchelonAssignWith(null)

/**
 * 原地通过行初等变换变为最简行阶梯型阵
 */
fun ValueMutableMatrix.simplifyAssign() =
	simplifyAssignWith(null)
