package org.mechdancer.algebra.implement.matrix.special

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.doubleEquals
import org.mechdancer.algebra.function.matrix.*
import org.mechdancer.algebra.uniqueValue

/**
 * 对角阵
 */
class DiagonalMatrix(
    val diagonal: List<Double>
) : SquareMatrix {
    init {
        require(diagonal.isNotEmpty())
    }

    override val dim get() = diagonal.size
    override fun get(r: Int, c: Int) = if (r == c) diagonal[r] else .0
    override fun row(r: Int) = getRow(r)
    override fun column(c: Int) = getColumn(c)
    override val rows get() = getRows()
    override val columns get() = getColumns()
    override val rank by lazy { diagonal.filter { it != .0 }.size }

    override val det by lazy {
        if (rank == diagonal.size)
            diagonal.fold(1.0) { r, i -> r * i }
        else .0
    }

    override val trace by lazy { diagonal.sum() }

    override fun equals(other: Any?) =
        this === other
        || (other is Matrix
            && checkSameSize(this, other)
            && ((other as? DiagonalMatrix)
                    ?.diagonal
                    ?.zip(other.diagonal, ::doubleEquals)
                    ?.all { it } == true
                ||
                other.filterIndexed { r, c, it ->
                    it != if (r == c) diagonal[r] else .0
                }.isEmpty()
               ))

    override fun hashCode() = diagonal.hashCode()

    override fun toString() = matrixView("${diagonal.size}d DiagonalMatrix")

    companion object {
        operator fun get(vararg elements: Number) =
            elements
                .map(Number::toDouble)
                .let { diagonal ->
                    diagonal
                        .uniqueValue()
                        ?.let { NumberMatrix[diagonal.size, it] }
                    ?: DiagonalMatrix(diagonal)
                }
    }
}
