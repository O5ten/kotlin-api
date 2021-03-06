package se.osten.utils

import org.litote.kmongo.MongoOperator
import se.osten.beans.Entity
import spark.Request
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

val bitsOf32: IntRange = 0..31
val dashes = listOf<Int>(8, 13, 18, 23, 32)

fun createGuid(): String {
    val rnd = Random(System.currentTimeMillis())
    val range: List<Int> = bitsOf32.map { rnd.nextInt(16) }
    return range.mapIndexed { v, i -> intToHex(v, i) }.joinToString("");
}

fun filterByTag(req: Request, sittpuffar: List<Entity>): List<Entity> {
    val tags = req.queryParams("tags")?.split(",")?.map { v -> v.toLowerCase().trim() }
    return if (tags != null) return sittpuffar.filter { v ->
        v.tags.map { t -> t.toLowerCase().trim() }.any { t ->
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
    println("API ${formatter.format(Date())} : ${req.ip()} ${req.requestMethod()} ${req.pathInfo()}/$event")
}

fun encode64(string: String): String {
    return String(Base64.getEncoder().encode(string.toByteArray(Charset.defaultCharset())))
}

fun toHashMap(puff: Entity): HashMap<String, Any> {
    return hashMapOf<String, Any>(
            "id" to puff.id,
            "name" to puff.name,
            "price" to puff.price,
            "imageLink" to puff.imageLink,
            "targetLink" to puff.targetLink,
            "tags" to puff.tags.joinToString(",")
    )
}

val byId = { id: String -> "{id: {${MongoOperator.eq}: '$id'}}" }


