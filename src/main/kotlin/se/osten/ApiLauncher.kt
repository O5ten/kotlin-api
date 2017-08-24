package se.osten

import se.osten.dao.MongoSittpuffDAO
import se.osten.paths.SittpuffPath
import spark.kotlin.port
import java.io.File
import java.io.FileInputStream
import java.util.*

fun main(args: Array<String>) {
    val properties = Properties();
    val file = File(".properties");
    val fis: FileInputStream = file.inputStream()
    properties.load(fis)
    fis.close()
    port(properties.getProperty("exposePort").toInt())
    SittpuffPath(MongoSittpuffDAO(properties), properties).activate()
}