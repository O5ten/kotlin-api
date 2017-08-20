package se.osten.dao

import org.litote.kmongo.*
import org.litote.kmongo.MongoOperator.*
import se.osten.api.SittpuffDAO
import se.osten.beans.Sittpuff

class MongoSittpuffDAO(val properties: Properties) : SittpuffDAO {

    val host = properties.getProperty("mongodb.host");
    val port = properties.getProperty("mongodb.port");
    val client = KMongo.createClient("$host:$port")
    val database = client.getDatabase("sittpuffar")
    val sittpuffar = database.getCollection<Sittpuff>("sittpuffar")

    val byId = { id: String -> "{id: {$eq: '$id'}}" }

    override fun count(): Long {
        return sittpuffar.count()
    }

    override fun findAll(): List<Sittpuff> {
        return sittpuffar.find().toList()
    }

    override fun save(sittpuff: Sittpuff) {
        sittpuffar.insertOne(sittpuff)
    }

    override fun findById(id: String): Sittpuff? {
        return sittpuffar.findOne(byId(id))
    }

    override fun findByName(name: String): Sittpuff? {
        return sittpuffar.findOne("{name: {\$eq: $name}}")
    }

    override fun update(id: String, sittpuff: Sittpuff) {
        sittpuffar.findOneAndReplace(byId(id), sittpuff)
    }

    override fun delete(id: String) {
        sittpuffar.deleteOne(byId(id))
    }
}

