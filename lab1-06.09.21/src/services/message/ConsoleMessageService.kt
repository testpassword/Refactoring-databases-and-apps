package services.message

class ConsoleMessageService: MessageService() {

    fun initSizeMessage() =
        println("Введите размерность матрицы(количество уравнений {n <= 20, n - целое число}).")

    fun invalidFormatSizeMessage() =
        System.err.println("Введенное значение должно быть целочисленным и содержать только цифры от 0 до 9!")

    fun wrongSizeMessage() =
        System.err.println("Количество уравнений должно быть положительным и меньше 20")

    fun initMatrixMessage() =
        println("""
            Введите элементы расширенной матрицы по следующему прицепу:${MessageColor.BLUE}
            * Каждый элемент должен быть разделен пробелом!
            * Запись производится по-строчно (строка должна содержать n + 1 элементов)!
            * Запись не должна содержать букв!
            * Целая часть должна отделяться от дробной точкой {.}!${MessageColor.RESET}
            """.trimIndent()
        )

    fun invalidFormatMatrixMessage() =
        System.err.println("Веденные вами данные не соответствуют условиям! Попробуйте ввести строку снова.")

    fun invalidMatrixLineMessage() =
        System.err.println("Количество элементов в строке расширенной матрицы не соответствует действительному! Попробуйте ввести строку снова.")
}