package services.work

import nonlinear_sys_equations.*
import nonlinear_sys_equations.Solvers.solveByGauss
import nonlinear_sys_equations.WrongSizeException
import services.Service
import services.message.MessageService
import java.io.FileNotFoundException
import java.io.FileReader
import java.util.*

class FileService: Service {

    private val ms = MessageService()
    private var size = 0
    private lateinit var matrixA: Array<DoubleArray>
    private lateinit var matrixB: DoubleArray
    private var reader: Scanner = Scanner(System.`in`)
    private var register = 0

    companion object {
        const val typeName = "файл"
        val TRY_AGAIN_MESSAGE = "${MessageService.MessageColor.WHITE}Проверьте правильность данных в файле и повторите попытку снова.${MessageService.MessageColor.RESET}"
    }

    @Throws(FileNotFoundException::class)
    fun initFile() {
        var key = false
        while (!key) {
            println("Введите абсолютный путь до файла. Если файл находится в директории с проектом, то можно ввести название файла.")
            FileReader(reader.nextLine().trim { it <= ' ' })
            while (true) {
                println("Проверьте название файла и выполнение всех условий. Хотите продолжить? ({да} | {нет})")
                val scanner1 = Scanner(System.`in`)
                val ans = scanner1.nextLine().trim { it <= ' ' }
                if (ans == "да") {
                    key = true
                    break
                } else if (ans == "нет") {
                    break
                } else System.err.println("Введите корректный ответ!")
            }
        }
    }

    @Throws(WrongSizeException::class, InvalidMatrixLineException::class)
    fun parseFile() {
        register = 1
        size = reader.nextLine().trim { it <= ' ' }.toInt()
        if (size in 21 downTo -1) throw WrongSizeException()
        register = 2
        matrixA = Array(size) { DoubleArray(size) }
        matrixB = DoubleArray(size)
        for (i in 0 until size) {
            val supMatrix = reader!!.nextLine().trim { it <= ' ' }.split(" ".toRegex()).toTypedArray()
            if (supMatrix.size == size + 1) {
                for (j in 0 until size) matrixA[i][j] = supMatrix[j].toDouble()
                matrixB[i] = supMatrix[size].toDouble()
            } else throw InvalidMatrixLineException()
        }
    }

    override fun execute() {
        println("""
            ${MessageService.MessageColor.RED}*******************************************************************${MessageService.MessageColor.RESET}
            Данные файла должны соответствовать следующим требованиям: ${MessageService.MessageColor.BLUE}
            * Первая строка должна содержать количество уравнений {n <= 20}.${MessageService.MessageColor.PURPLE}
            * Далее следует n строк, содержащие элементы расширенной матрицы.
            # Каждый элемент разделен пробелом от другого пробелом.
            # Целая часть должна отделяться от дробной точкой {.}!${MessageService.MessageColor.RESET}
            
            Пример:${MessageService.MessageColor.GREEN}
                2
                1 2 10
                5 6 11
                ${MessageService.MessageColor.RED}*******************************************************************${MessageService.MessageColor.RESET}
        """)
        while (true) {
            try {
                initFile()
                parseFile()
                break
            } catch (e: Exception) {
                System.err.println(
                    when (e) {
                        is FileNotFoundException -> "Введенный вами файл не существует или в его названии ошибка!"
                        is NumberFormatException -> when (register) {
                            1 -> "Недопустимые символы в размере матрицы!"
                            2 -> "Недопустимые символы в элементах матрицы!"
                            else -> ""
                        }
                        is NoSuchElementException -> when (register) {
                            1 -> "В файле не установлен размер!"
                            2 -> "В файле заданы не все элементы расширенной матрицы!"
                            else -> ""
                        }
                        else -> "Неизвестная ошибка: ${e.message}"
                    } + TRY_AGAIN_MESSAGE
                )
            }
        }
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