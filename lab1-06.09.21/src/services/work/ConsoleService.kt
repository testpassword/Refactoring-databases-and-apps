package services.work

import nonlinear_sys_equations.*
import nonlinear_sys_equations.Solvers.solveByGauss
import services.message.ConsoleMessageService
import java.lang.NumberFormatException
import services.Service
import java.util.*

open class ConsoleService(type: String = "консоль"): Service {

    private val ms = ConsoleMessageService().apply { selectedTypeMessage(type) }
    var size = 0
    lateinit var matrixA: Array<DoubleArray>
    lateinit var matrixB: DoubleArray

    private fun initSize() {
        val scanner = Scanner(System.`in`)
        ms.initSizeMessage()
        while (true) {
            try {
                size = scanner.nextLine().toInt()
                if (size in 1..20) break else ms.wrongSizeMessage()
            } catch (e: NumberFormatException) {
                ms.invalidFormatSizeMessage()
            }
        }
    }

    open fun initMatrix() {
        val scanner = Scanner(System.`in`)
        matrixA = Array(size) { DoubleArray(size) }
        matrixB = DoubleArray(size)
        ms.initMatrixMessage()
        for (i in 0 until size) {
            var count = 0
            while (count < size + 1) {
                val supMatrix = scanner.nextLine().trim { it <= ' ' }.split(" ".toRegex()).toTypedArray()
                if (supMatrix.size == size + 1) {
                    try {
                        for (j in 0 until size) {
                            matrixA[i][j] = supMatrix[j].toDouble()
                            count++
                        }
                        matrixB[i] = supMatrix[size].toDouble()
                        count++
                    } catch (e: NumberFormatException) {
                        ms.invalidFormatMatrixMessage()
                        count = 0
                    }
                } else ms.invalidMatrixLineMessage()
            }
        }
    }

    override fun execute() {
        initSize()
        initMatrix()
        println(try {
            """
                ${Formatters.formatMatrixSize(matrixA)}
                ${Formatters.showMatrixMessage(matrixA, matrixB, false)}
                ${Formatters.formatNonlinearSysEquations(solveByGauss(matrixA to matrixB))}
            """.trimIndent()
        } catch (e: Exception) { e.message }
        )
    }
}