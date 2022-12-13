package hw1

val processes = arrayListOf<Process>()

fun main() {

    var selectedOptions: Int = 1

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
            1 -> addNewProcess()
            2 -> showAllProcesses()
            3 -> showWithPriority()
            else -> break
        }
    }
}

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
    val processesWithPriority = processes
    processesWithPriority.sortBy { it.priority }
    processesWithPriority.forEach {
        println(it)
    }
}
