package nonlinear_sys_equations

import services.message.MessageService

object Formatters {

    private const val DIVIDER = "\n.............................................."

    fun showMatrixMessage(matrixA: Array<DoubleArray>, matrixB: DoubleArray, conversion: Boolean) =
        """
            $DIVIDER
            ${if (conversion) "Расширенная матрица элементов после преобразования: " else "Расширенная матрица элементов будет выглядеть следующим образом: "}
            ${matrixA.mapIndexed { i, it -> "\n${MessageService.MessageColor.GREEN}${it.contentToString()} ${MessageService.MessageColor.CYAN}${matrixB[i]}${MessageService.MessageColor.RESET}" }.joinToString()}
        """.trimIndent()

    fun formatMatrixSize(matrix: Array<DoubleArray>) =
        """
            $DIVIDER
            Размерность: ${MessageService.MessageColor.GREEN}${matrix.size}${MessageService.MessageColor.RESET}
        """.trimIndent()

    fun formatNonlinearSysEquations(systemEquations: Triple<DoubleArray, DoubleArray, Double>): String =
        formatUnknowns(systemEquations.first) + formatResidual(systemEquations.second) + formatDetermToString(systemEquations.third)

    fun formatDetermToString(det: Double): String =
        """
            $DIVIDER
            Определитель матрицы: 
            ${MessageService.MessageColor.GREEN}$det${MessageService.MessageColor.RESET}
        """.trimIndent()

    fun formatUnknowns(res: DoubleArray): String =
        """
            $DIVIDER
            Таблица неизвестных:
            ${res.mapIndexed { i, it -> "\nx${i + 1} = ${MessageService.MessageColor.GREEN}${it}${MessageService.MessageColor.RESET}" }.joinToString()}
        """.trimIndent()

    fun formatResidual(red: DoubleArray): String =
        """
            $DIVIDER
            Таблица невязок:
            ${red.joinToString { "\n| ${MessageService.MessageColor.GREEN}$it${MessageService.MessageColor.RESET}|" }}
        """.trimIndent()
}