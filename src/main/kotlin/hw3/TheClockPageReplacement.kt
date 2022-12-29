package hw3

const val EMPTY_PAGE_ID = -1
data class Page(
    var id: Int = EMPTY_PAGE_ID,
    var modified: Int = 0,
    var referenced: Int = 0
)
fun main(){
    val theClockPageReplacement = TheClockPageReplacement()
    var selectedOption: Int
    while(true) {

        println(
            "_________________________________________________________________________________\n" +
                    "Select options: \n" +
                    "1 - Add new page \n" +
                    "2 - Reference page by id \n" +
                    "3 - Modify page data \n" +
                    "4 - Delete page \n" +
                    "5 - Make all page MODIFIED and REFERENCED bits as 0 \n" +
                    "6 - Show current memory state \n" +
                    "7 - Quit \n" +
                    "_________________________________________________________________________________" +
                    " \n")

        selectedOption = readln().toIntOrNull() ?: 1

        when (selectedOption) {
            1 -> theClockPageReplacement.addPage()
            2 -> theClockPageReplacement.referencePageById()
            3 -> theClockPageReplacement.modifyPageData()
            4 -> theClockPageReplacement.deletePage()
            5 -> theClockPageReplacement.makeReferencedAndModifiedAsDefault()
            6 -> theClockPageReplacement.showCurrentMemoryState()
            7 -> break
            else -> println("\nEnter 7 to quit")
        }
    }
}

class TheClockPageReplacement {

    companion object {
        const val FISCAL_CAPACITY = 4
        const val SWAPPING_CAPACITY = 6
    }

    private val fiscalMemory = ArrayList<Page>(FISCAL_CAPACITY)
    private val swappingMemory = ArrayList<Page>(SWAPPING_CAPACITY)
    private val pageTable = arrayListOf<Int>()
    private var clockIndex: Int = -1

    init {
        repeat(FISCAL_CAPACITY) {
            fiscalMemory.add(Page())
        }
        repeat(SWAPPING_CAPACITY) {
            swappingMemory.add(Page())
        }
    }
    fun addPage() {
        if (fiscalMemory.all { it.id != EMPTY_PAGE_ID } && swappingMemory.all { it.id != -1 }) {
            println("Can't add new page, there is no empty space on Fiscal and Swapping Memories")
            return
        }

        if (fiscalMemory.any { it.id == EMPTY_PAGE_ID }) {
            addPageExceptEvict()
            return
        }
        addPageWithEvict()
    }

    private fun addPageExceptEvict() {
        var newPageInd = if (clockIndex >= fiscalMemory.lastIndex) 0 else (clockIndex + 1)
        while (true) {
            if (fiscalMemory[newPageInd].id == EMPTY_PAGE_ID) {
                fiscalMemory[newPageInd].id = pageTable.size
                clockIndex = newPageInd
                pageTable.add(pageTable.size)
                println("New page id = ${fiscalMemory[newPageInd].id} added in FiscalMemory at index = $newPageInd")
                return
            } else {
                fiscalMemory[newPageInd].referenced = 0
            }
            if (newPageInd == fiscalMemory.lastIndex) {
                newPageInd = 0
            } else {
                newPageInd++
            }
        }
    }

    private fun addPageWithEvict() {
        val newSwappingPageInd = swappingMemory.indexOfFirst { it.id == EMPTY_PAGE_ID }
        var updatedPageInd = if (clockIndex >= fiscalMemory.lastIndex) 0 else (clockIndex + 1)
        while (true) {
            if (fiscalMemory[updatedPageInd].referenced == 0) {
                val oldPage = fiscalMemory[updatedPageInd]
                fiscalMemory[updatedPageInd] = Page(id = pageTable.size)
                swappingMemory[newSwappingPageInd] = oldPage
                clockIndex = updatedPageInd
                pageTable.add(pageTable.size)
                println("New page id = ${fiscalMemory[updatedPageInd].id} added in FiscalMemory at index = $updatedPageInd")
                return
            } else {
                fiscalMemory[updatedPageInd].referenced = 0
            }
            if (updatedPageInd == fiscalMemory.lastIndex) {
                updatedPageInd = 0
            } else {
                updatedPageInd++
            }
        }
    }

    fun referencePageById() {
        println("Enter page id")
        val id = readln().toInt()
        val page = fiscalMemory.firstOrNull { it.id == id }
        if (page != null) {
            page.referenced = 1
            println("Pages reference successfully referenced")
        } else {
            println("Page fault")
            val replacingSwapIndex = swappingMemory.indexOfFirst { it.id == id }
            if (replacingSwapIndex != -1) {
                replaceFromSwappingToFiscal(replacingSwapIndex)
                println("Pages successfully replaced")
            } else {
                println("Page not founded")
            }
        }
    }

    private fun replaceFromSwappingToFiscal(swapMemoryInd: Int) {
        var fiscalReplaceInd = if (clockIndex >= fiscalMemory.lastIndex) 0 else (clockIndex + 1)
        //val tempPage = fiscalMemory[fiscalReplaceInd]
//        fiscalMemory[fiscalReplaceInd] = swappingMemory[swapMemoryInd].copy(modified = 0, refenrenced = 0)
//        swappingMemory[swapMemoryInd] = tempPage.copy(modified = 0, refenrenced = 0)

        while (true) {
            if (fiscalMemory[fiscalReplaceInd].referenced == 0) {
                val tempPage = fiscalMemory[fiscalReplaceInd]
                fiscalMemory[fiscalReplaceInd] = swappingMemory[swapMemoryInd].copy(modified = 0, referenced = 0)
                swappingMemory[swapMemoryInd] = tempPage.copy(modified = 0, referenced = 0)
                clockIndex = fiscalReplaceInd
                return
            } else {
                fiscalMemory[fiscalReplaceInd].referenced = 0
            }
            if (fiscalReplaceInd == fiscalMemory.lastIndex) {
                fiscalReplaceInd = 0
            } else {
                fiscalReplaceInd++
            }
        }
    }

    fun modifyPageData() {
        println("Enter page id")
        val id = readln().toInt()
        val page = fiscalMemory.firstOrNull { it.id == id }
        if (page == null) {
            println("There's no page as id = $id in Fiscal Memory")
        } else {
            page.modified = 1
            println("Page data was updated")
        }
    }

    fun deletePage() {
        println("Enter page id")
        val id = readln().toInt()
        var index = fiscalMemory.indexOfFirst { it.id == id }
        if (index == -1) {
            println("There's no page as id = $id in Fiscal Memory")
        } else {
            fiscalMemory[index] = Page()
            println("Page successfully was deleted")
        }
    }

    fun makeReferencedAndModifiedAsDefault() {
        fiscalMemory.forEach {
            it.modified = 0
            it.referenced = 0
        }
        swappingMemory.forEach {
            it.modified = 0
            it.referenced = 0
        }
        println("Referenced and Modified of Page Table was successfully updated as 0")
    }

    fun showCurrentMemoryState() {
        println("Current Clock index = $clockIndex")
        println("Fiscal Memory")
        fiscalMemory.forEachIndexed { index, page ->
            println("$index -> $page")
        }
        println("\nSwapping Memory")
        swappingMemory.forEachIndexed { index, page ->
            println("$index -> $page")
        }
    }
}