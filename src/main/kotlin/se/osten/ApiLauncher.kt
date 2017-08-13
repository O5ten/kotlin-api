package se.osten

import se.osten.dao.MongoSittpuffDAO
import se.osten.paths.SittpuffPath
import spark.kotlin.port

fun main(args: Array<String>) {
    port(4567)
    SittpuffPath(MongoSittpuffDAO()).activate()
}