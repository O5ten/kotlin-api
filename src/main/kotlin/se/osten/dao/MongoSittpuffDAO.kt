package se.osten.dao

import com.mongodb.MongoClientURI
import org.litote.kmongo.*
import org.litote.kmongo.MongoOperator.*
import org.litote.kmongo.util.KMongoConfiguration
import se.osten.beans.Sittpuff

class MongoSittpuffDAO : SittpuffDAO {

    val host = System.getProperty("mongodb.host", "localhost");
    val port = System.getProperty("mongodb.port", "27017")
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

