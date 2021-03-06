package org.mechdancer.geometry.transformation

import org.junit.Test
import org.mechdancer.algebra.implement.vector.vector2D
import org.mechdancer.algebra.implement.vector.vector2DOfZero
import org.mechdancer.geometry.angle.toRad
import kotlin.math.PI
import kotlin.test.assertEquals

class TestPose2D {
    /**
     * 测试里程计算法
     */
    @Test
    fun test() {
        val step0 = pose2D()
        assert(step0 == Pose2D(vector2DOfZero(), .0.toRad())) {
            "里程计初始化错误：$step0"
        }

        val delta1 = Pose2D(vector2D(3.0, 4.0), (PI / 2).toRad())

        val step1 = step0 plusDelta delta1
        assert(step1 == delta1) {
            "里程计累加错误：$step1 ≠ $delta1"
        }

        val step2 = step1 minusDelta delta1
        assert(step2 == step0) {
            "里程计回滚错误：$step2 ≠ $step0"
        }

        val delta2 = Pose2D(vector2D(1.0, 1.0), (-PI / 4).toRad())

        val step3 = step2 plusDelta delta1
        val step4 = step3 plusDelta delta2 minusState step3
        assert(step4 == delta2) {
            "里程计标记错误：$step4 ≠ $delta2"
        }
    }

    @Test
    fun testToMatrix() {
        val a = pose2D(1, -2, 0)
        assertEquals(a, a.toMatrixTransformation().toPose2D())
    }
}
