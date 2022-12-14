package hw1

val processes = arrayListOf<Process>()

fun main() {

    var selectedOptions: Int = 1
    val hW1 = HW1()

    while(true) {

        println(
            "_________________________________________________________________________________\n" +
                    "Select options: \n " +
                    "1 - Add new process \n " +
                    "2 - Show all processes \n " +
                    "3 - Show processes with priority \n " +
                    "4 - Quit \n" +
                    "_________________________________________________________________________________" +
                    " \n\n")

        selectedOptions = readln().toIntOrNull() ?: 4

        when (selectedOptions) {
            1 -> hW1.addNewProcess()
            2 -> hW1.showAllProcesses()
            3 -> hW1.showWithPriority()
            else -> break
        }
    }
}

class HW1 {
    fun addNewProcess() {
        println("Please enter name and priority of new process by white space \n")
        val (name, priority) = readln().split(' ')

        processes.add(
            hw1.Process(
                name = name,
                priority = priority.toInt()
            )
        )
        println("Successfully added\n")
    }

    fun showAllProcesses() {
        println("All processes: \n")
        processes.forEach {
            println(it)
        }
    }

    fun showWithPriority() {
        println("Processes with priority \n")
        val processesWithPriority = arrayListOf<Process>()
        processesWithPriority.addAll(processes)
        processesWithPriority.sortBy { it.priority }
        processesWithPriority.forEach {
            println(it)
        }
    }
}
