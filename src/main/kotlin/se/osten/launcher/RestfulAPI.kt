package se.osten.launcher

import com.google.gson.Gson
import se.osten.beans.Sittpuff
import se.osten.dao.InMemorySittpuffDAO
import se.osten.dao.MongoSittpuffDAO
import se.osten.utils.createGuid
import se.osten.utils.filterByTag
import spark.Spark.*

fun main(args: Array<String>) {

    val dao = MongoSittpuffDAO()
    val gson = Gson()

    path("/sittpuffar") {

        get("") { req, res ->
            println("API: /sittpuffar ${dao.count()} results delivered")
            res.type("application/json")
            gson.toJson(filterByTag(req, dao.findAll()))
        }

        get("/:id") { req, res ->
            res.type("application/json")
            val sittpuff: Sittpuff? = dao.findById(req.params("id"))
            if (sittpuff != null) {
                println("API: /sittpuffar/${req.params("id")} found")
                gson.toJson(sittpuff)
            } else {
                println("API: /sittpuffar/${req.params("id")} not found")
                res.status(404)
                ""
            }
        }

        post("") { req, res ->
            val fromJson: Sittpuff = gson.fromJson(req.body(), Sittpuff::class.java)
            val id: String = createGuid()
            dao.save(fromJson.copy(id))
            println("API: /sittpuffar/${id} created")
            res.status(201)
            "$id created"
        }

        put("/:id") { req, res ->
            val sittpuff: Sittpuff = gson.fromJson(req.body(), Sittpuff::class.java)
            println("API: /sittpuffar/${sittpuff.id} modified")
            dao.update(req.params("id"), sittpuff)
            ""
        }

        delete("/:id") { req, res ->
            println("API: /sittpuffar/${req.params("id")} removed")
            dao.delete(req.params("id"))
            ""
        }
    }
}