package se.osten.utils

import java.util.*

val bitsOf32: IntRange = 0..31
val dashes = listOf<Int>(8, 13, 18, 23, 32)

fun createGuid(): String {
    val rnd: Random = Random(System.currentTimeMillis())
    val range: List<Int> = bitsOf32.map { rnd.nextInt(16) }
    return range.mapIndexed { v, i -> intToHex(v, i) }.joinToString("");
}

fun intToHex(index: Int, key: Int): String {
    if (index in dashes) {
        return "-"
    }
    return when(key) {
        10 -> "a"
        11 -> "b"
        12 -> "c"
        13 -> "d"
        14 -> "e"
        15 -> "f"
        else -> key.toString()
    }
}


