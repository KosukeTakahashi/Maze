import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.todo

val xSize = 16
val ySize = 16
val field = Array(ySize, {_ -> BooleanArray(xSize, {_ -> true})})
val ways = ArrayList<Pair<Int, Int>>()

fun main(args: Array<String>) {
    printMaze()
    println("===")
    firstStep()
    for (i in 0..1024)
        firstStep(false)
    printMaze()
}

fun firstStep(isFirst: Boolean = true) {
    var next =
            if (isFirst)
                random()
            else
                select()

    println("${if (isFirst) "random" else "select"}() => ${next.first}, ${next.second}")

    while (canExtend(next)) {
        extend(next)
        next = nextPoint(next)
    }
}

fun canExtend(target: Pair<Int, Int>): Boolean {
    todo { "端っこまで伸びるように" }
    val x = target.first
    val y = target.second

    return if (x < 0 || y < 0) {
        false
    } else if (get(x, y)) {
        val nears = arrayOf(
                Pair(x + 1, y),
                Pair(x, y + 1),
                Pair(x - 1, y),
                Pair(x, y - 1))
        3 <= wallCount(nears)
    } else {
        false
    }
}

fun wallCount(pnts: Array<Pair<Int, Int>>): Int {
    return try {
        pnts.count { get(it) }
    } catch (e: ArrayIndexOutOfBoundsException) {
        -1
    }
}

fun nextPoint(current: Pair<Int, Int>): Pair<Int, Int> {
    val x = current.first
    val y = current.second
    val rdm = Random()
    val dir = rdm.nextInt(3)
    val nxt = when(dir) {
        0 -> Pair(x + 1, y)
        1 -> Pair(x, y + 1)
        2 -> Pair(x - 1, y)
        3 -> Pair(x, y - 1)
        else -> Pair(-99, -99)
    }

    try {
        get(nxt)
    } catch (e: ArrayIndexOutOfBoundsException) {
        return Pair(-99, -99)
    }

    return nxt
}

fun random(): Pair<Int, Int> {
    val rdm = Random()
    val x = rdm.nextInt(xSize)
    val y = rdm.nextInt(ySize)

    return if (get(x, y))
        Pair(x, y)
    else
        Pair(-99, -99)
}

fun select(): Pair<Int, Int> {
    val rdm = Random()
    val idx = rdm.nextInt(ways.size)
    val pnt = ways[idx]
    return pnt
}

fun printMaze() {
    for (i in 0..xSize)
        print("■")
    println("■")

    for (col in field) {
        print("■")
        for (pnt in col)
            print(if (pnt) "■" else "　")
        println("■")
    }

    for (i in 0..xSize)
        print("■")
    println("■")
}

fun extend(pnt: Pair<Int, Int>) {
    inv(pnt)
    ways.add(pnt)
}

fun get(pnt: Pair<Int, Int>) = get(pnt.first, pnt.second)

fun get(x: Int, y: Int) = field[ySize - y - 1][x]

fun inv(pnt: Pair<Int, Int>) {
    inv(pnt.first, pnt.second)
}

fun inv(x: Int, y: Int) {
    field[ySize - y - 1][x] = !field[ySize - y - 1][x]
}
