package se.osten.utils

import se.osten.beans.Sittpuff
import spark.Request
import java.text.SimpleDateFormat
import java.util.*

val bitsOf32: IntRange = 0..31
val dashes = listOf<Int>(8, 13, 18, 23, 32)

fun createGuid(): String {
    val rnd: Random = Random(System.currentTimeMillis())
    val range: List<Int> = bitsOf32.map { rnd.nextInt(16) }
    return range.mapIndexed { v, i -> intToHex(v, i) }.joinToString("");
}

fun filterByTag(req: Request, sittpuffar: List<Sittpuff>): List<Sittpuff> {
    val tags = req.queryParams("tags")?.split(",")?.map { v -> v.toLowerCase() }
    return if (tags != null) return sittpuffar.filter { v ->
        v.tags.map { t -> t.toLowerCase() }.any { t ->
            t in tags
        }
    } else sittpuffar
}

fun intToHex(index: Int, key: Int): String {
    if (index in dashes) {
        return "-"
    }
    return when (key) {
        10 -> "a"
        11 -> "b"
        12 -> "c"
        13 -> "d"
        14 -> "e"
        15 -> "f"
        else -> key.toString()
    }
}

val formatter = SimpleDateFormat("HH:mm:ss")
fun log(req: Request, event: String = "") {
    println("API ${formatter.format(Date())} : ${req.ip()} ${req.requestMethod()} /sittpuffar/$event")
}

fun toHashMap(puff: Sittpuff): HashMap<String, Any> {
    return hashMapOf<String, Any>(
            "id" to puff.id,
            "name" to puff.name,
            "price" to puff.price,
            "imageLink" to puff.imageLink,
            "targetLink" to puff.targetLink,
            "tags" to puff.tags.joinToString(",")
    )
}

