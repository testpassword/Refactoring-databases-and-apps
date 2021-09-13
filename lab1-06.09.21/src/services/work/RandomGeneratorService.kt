package services.work

import nonlinear_sys_equations.Solvers.generateRandomMatrix

class RandomGeneratorService: ConsoleService(TYPE_NAME) {

    companion object { private const val TYPE_NAME = "генерация случайных чисел" }

    override fun initMatrix() {
        val (matrixA, matrixB) = generateRandomMatrix(super.size)
        super.matrixA = matrixA
        super.matrixB = matrixB
    }
}