package se.osten.beans

import org.litote.kmongo.KMongo
import java.util.*

data class DatabaseConnection(val properties: Properties) {
    val host = properties.getProperty("mongodb.host");
    val port = properties.getProperty("mongodb.port");
    val client = KMongo.createClient("$host:$port")
    val database = client.getDatabase("sittpuffar")
}