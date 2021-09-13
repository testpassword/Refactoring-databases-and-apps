package nonlinear_sys_equations

import kotlin.math.roundToInt

typealias ExtendedMatrix = Pair<Array<DoubleArray>, DoubleArray>

object Solvers {

    fun generateRandomMatrix(size: Int): ExtendedMatrix {
        val genRandVector = { DoubleArray(size) { (Math.random() * 100000).roundToInt() / 1000.0 } }
        return Array(size) { genRandVector() } to genRandVector()
    }

    fun calcDeterminant(matrix: Array<DoubleArray>): Double = matrix.mapIndexed { i, it -> it[i] }.reduce { acc, it -> acc * it }

    fun swapVectors(extMatrix: ExtendedMatrix, replacedVecNumber: Int, substitutionVecNumber: Int) {
        val (matrix, solutionsVector) = extMatrix
        for (i in matrix.indices) matrix[replacedVecNumber][i] = matrix[substitutionVecNumber][i].also { matrix[substitutionVecNumber][i] = matrix[replacedVecNumber][i] }
        solutionsVector[replacedVecNumber] = solutionsVector[substitutionVecNumber].also { solutionsVector[substitutionVecNumber] = solutionsVector[replacedVecNumber] }
    }

    fun calcResidual(extMatrix: ExtendedMatrix, result: DoubleArray): DoubleArray =
        extMatrix.second.mapIndexed { i, sol -> sol - result.mapIndexed { j, res -> extMatrix.first[i][j] * res }.sum() }.toDoubleArray()

    fun Array<DoubleArray>.deepCopy(): Array<DoubleArray> = this.map(DoubleArray::copyOf).toTypedArray()

    fun checkExtMatrixSize(extMatrix: ExtendedMatrix): Boolean = extMatrix.first.size == extMatrix.second.size

    fun solveByGauss(extMatrix: ExtendedMatrix): Triple<DoubleArray, DoubleArray, Double> {
        val clonedMatrix = extMatrix.first.deepCopy()
        val clonedSolutionsVector = extMatrix.second.copyOf()
        val size = if (checkExtMatrixSize(extMatrix)) clonedMatrix.size else throw WrongSizeException()
        var determ = 1.0
        val result = DoubleArray(size)
        val onLeaderFound: (Int) -> Unit = {
            var status = false
            if (clonedMatrix[it][it] == 0.0) {
                for (i in it + 1 until size)
                    if (clonedMatrix[i][it] != 0.0) {
                        swapVectors(clonedMatrix to clonedSolutionsVector, i, it)
                        status = true
                        determ *= -1.0
                        break
                    }
                if (!status) throw CannotSolveException()
            }
        }
        //строки, которые вычитаем
        for (i in 0 until size - 1) {
            onLeaderFound(i)
            //строки из которой вычитаем
            for (j in i + 1 until size) {
                val c = clonedMatrix[j][i] / clonedMatrix[i][i]
                clonedMatrix[j][i] = 0.0
                for (k in i + 1 until size) clonedMatrix[j][k] -= c * clonedMatrix[i][k]
                clonedSolutionsVector[j] -= c * clonedSolutionsVector[i]
            }
        }
        if (clonedMatrix[size - 1][size - 1] == 0.0 && clonedSolutionsVector[size - 1] != 0.0) throw EndlessSolutionException()
        onLeaderFound(size - 1)
        for (i in size - 1 downTo 0) result[i] = (clonedSolutionsVector[i] - result.mapIndexed { j, it -> it * clonedMatrix[i][j] }.sum()) / clonedMatrix[i][i]
        determ = calcDeterminant(clonedMatrix)
        val residual = calcResidual(clonedMatrix to clonedSolutionsVector, result)
        return Triple(result, residual, determ)
    }
}