package services.message

import services.Service

open class MessageService: Service {

    internal object MessageColor {
        const val RESET = "\u001B[0m"
        const val RED = "\u001B[31m"
        const val GREEN = "\u001B[32m"
        const val BLUE = "\u001B[34m"
        const val PURPLE = "\u001B[35m"
        const val CYAN = "\u001B[36m"
        const val WHITE = "\u001B[37m"
    }

    fun printDataEntryMethodMessage() =
        println("Выберите способ ввода данных (консоль {к}| файл {ф}| случайная генерация чисел {г})")

    fun printWrongInputDataTypeMessage() =
        System.err.println("Проверьте название способа ввода данных! Запись должна содержать единственный символ.")

    fun selectedTypeMessage(type: String) =
        println("Ввод данных осуществляется через: " + MessageColor.RED + type + MessageColor.RESET)

    override fun execute() =
        println("Добро пожаловать! Данная программа решает СЛАУ методом Гаусса.")
}