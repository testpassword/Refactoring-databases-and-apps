import services.Service
import services.message.MessageService
import services.work.ConsoleService
import services.work.FileService
import services.work.RandomGeneratorService
import java.util.*

object Controller {

    private var dataEntryMethod: String = ""
    private var correctDataStatus = false
    private val messageService = MessageService().also { it.execute() }

    fun createService(serviceName: String): Service =
        when (serviceName) {
            "к" -> ConsoleService()
            "ф" -> FileService()
            "г" -> RandomGeneratorService()
            else -> throw Exception("Неизвестный ключ")
        }

    operator fun invoke() {
        enterDataEntryMethod()
        correctDataStatus = isCorrectDataStatus()
        while (!correctDataStatus) {
            messageService.printWrongInputDataTypeMessage()
            enterDataEntryMethod()
            correctDataStatus = isCorrectDataStatus()
        }
        createService(dataEntryMethod).execute()
    }

    private fun enterDataEntryMethod() {
        messageService.printDataEntryMethodMessage()
        dataEntryMethod = Scanner(System.`in`).nextLine().trim { it <= ' ' }.lowercase()
    }

    private fun isCorrectDataStatus(): Boolean =
        dataEntryMethod == "к" || dataEntryMethod == "ф" || dataEntryMethod == "г"
}