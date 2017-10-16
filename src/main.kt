import java.util.*
import kotlin.collections.ArrayList

val xSize = 48
val ySize = 48
val correction = Math.max(xSize, ySize) / 16.0 / 100.0
val field = Array(ySize, {_ -> BooleanArray(xSize, {_ -> true})})
val ways = ArrayList<Pair<Int, Int>>()

fun main(args: Array<String>) {
    step()
    while ((ways.size.toDouble() / (xSize * ySize).toDouble()) < (0.5 + correction))
        step(false)
    printMaze()
    println((ways.size.toDouble()) / (xSize * ySize).toDouble())
}

fun step(isFirst: Boolean = true) {
    var next =
            if (isFirst)
                random()
            else
                nextPoint(select())

    while (canExtend(next)) {
        extend(next)
        next = nextPoint(next)
    }
}

fun canExtend(target: Pair<Int, Int>): Boolean {
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
    val nears = listOf(
            Pair(x + 1, y),
            Pair(x, y + 1),
            Pair(x - 1, y),
            Pair(x, y - 1)).shuffle()
    val nxt = nears[0]

    try {
        get(nxt)
    } catch (e: ArrayIndexOutOfBoundsException) {
        return Pair(-99, -99)
    }

    return nxt
}

fun random(): Pair<Int, Int> {
    val rdm = Random()
    val x = rdm.nextInt(xSize - 1) + 1
    val y = rdm.nextInt(ySize - 1) + 1

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
    for (col in field) {
        for (pnt in col)
            print(if (pnt) "■" else "　")
        println()
    }
}

fun <T> List<T>.shuffle(): List<T> {
    val list = this.toMutableList()
    java.util.Collections.shuffle(list)
    return list
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
