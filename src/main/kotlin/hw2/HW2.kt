package hw2

fun main() {

    var selectedOptions: Int = 1

    val hw2 = HW2()

    while(true) {

        println(
            "_________________________________________________________________________________\n" +
                    "Select options: \n " +
                    "1 - Add new process \n " +
                    "2 - Stop some processes \n " +
                    "3 - Show current memory allocation \n " +
                    "4 - Quit \n" +
                    "_________________________________________________________________________________" +
                    " \n\n")

        selectedOptions = readln().toIntOrNull() ?: 4
        when (selectedOptions) {
            1 -> hw2.addNewProcess()
            2 -> hw2.stopProcess()
            3 -> hw2.showCurrentMemoryAllocation()
            else -> break
        }
    }
}
class HW2 {

    private val MEMORY_CAPACITY = 64

    private val processes = arrayListOf<Process>()
    private val memoryAllocate = arrayListOf<Int>()
    private var lastProcessAddedIndex = -1

    init {
        repeat(MEMORY_CAPACITY) {
            memoryAllocate.add(0)
        }
    }

    fun addNewProcess() {
        println("Please enter memory ALLOCATE and NAME of new process by white space \n")
        val (allocateInput, name) = readln().split(' ')

        val allocate = allocateInput.toInt()

        var countToStopWhile = 0

        if (memoryAllocate.size < allocate) {
            println("There is no empty space")
            return
        }

        var tempCheckedSpaceIndex = if (lastProcessAddedIndex < memoryAllocate.lastIndex) (lastProcessAddedIndex + 1) else 0

        var lastAllocatedIndex = 0
        var enoughSpaceCount = 0
        while (tempCheckedSpaceIndex != lastProcessAddedIndex) {

            //countForStopWhile++


            if (memoryAllocate[tempCheckedSpaceIndex] == 0) {
                enoughSpaceCount++
            } else {
                enoughSpaceCount = 0
            }

            //println("TI = $tempCheckedSpaceIndex  and value = ${memoryAllocate.get(tempCheckedSpaceIndex)}  and enoughtSpcae = $enoughSpaceCount and LastAddInd = $lastProcessAddedIndex")

            if (enoughSpaceCount == allocate) {
                lastAllocatedIndex = tempCheckedSpaceIndex
                lastProcessAddedIndex = lastAllocatedIndex
                val firstAllocatedIndex = (lastAllocatedIndex - allocate + 1)

                //println("first = $firstAllocatedIndex and last = $lastAllocatedIndex  and lastProcAdd = $lastProcessAddedIndex")

                for (i in firstAllocatedIndex..lastAllocatedIndex) {
                    memoryAllocate[i] = 1
                }

                processes.add(
                    hw2.Process(
                        allocate = allocate,
                        name = name,
                        firstAllocatedIndex = firstAllocatedIndex,
                        lastAllocatedIndex = lastAllocatedIndex
                    )
                )
                println("Successfully added $name with allocate = $allocate\n")
                return
            }

            if (tempCheckedSpaceIndex == memoryAllocate.lastIndex) {
                enoughSpaceCount = 0
                tempCheckedSpaceIndex = 0
            } else {
                tempCheckedSpaceIndex++
            }
        }

        println("There is no empty space !!!")
        return
    }

    fun stopProcess() {
        println("Enter some process name to stop: \n")
        val name = readln()
        val process = processes.find { it.name == name }
        if (process != null) {
            for (i in process.firstAllocatedIndex..process.lastAllocatedIndex) {
                memoryAllocate[i] = 0
            }
            processes.remove(process)
            println("Successfully stopped process")
        } else {
            println("No process was found, try it again ")
        }
    }

    fun showCurrentMemoryAllocation() {
        println(ArrayList(memoryAllocate))

        processes.forEach {
            println(it)
        }
    }
}