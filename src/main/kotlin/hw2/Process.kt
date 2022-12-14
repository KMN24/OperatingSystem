package hw2

data class Process(
    val allocate: Int,
    val name: String,
    val firstAllocatedIndex: Int,
    val lastAllocatedIndex: Int
)