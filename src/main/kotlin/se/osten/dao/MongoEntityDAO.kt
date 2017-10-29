package se.osten.dao

import org.litote.kmongo.*
import org.litote.kmongo.MongoOperator.eq
import se.osten.api.DAO
import se.osten.beans.DatabaseConnection
import se.osten.beans.Entity
import se.osten.utils.byId
import java.util.*

class MongoEntityDAO(val dbConn: DatabaseConnection, collectionName: String) : DAO<Entity> {

    val entities = dbConn.database.getCollection<Entity>(collectionName)

    override fun count(): Long {
        return entities.count()
    }

    override fun findAll(): List<Entity> {
        return entities.find().toList()
    }

    override fun save(sittpuff: Entity) {
        entities.insertOne(sittpuff)
    }

    override fun findById(id: String): Entity? {
        return entities.findOne(byId(id))
    }

    override fun findByName(name: String): Entity? {
        return entities.findOne("{path: {\$eq: $name}}")
    }

    override fun update(id: String, sittpuff: Entity) {
        entities.findOneAndReplace(byId(id), sittpuff)
    }

    override fun delete(id: String) {
        entities.deleteOne(byId(id))
    }
}

