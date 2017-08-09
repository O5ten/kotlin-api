package se.osten.launcher

import com.google.gson.Gson
import se.osten.beans.Sittpuff
import se.osten.dao.SittpuffDao
import se.osten.utils.createGuid
import se.osten.utils.filterByTag
import spark.Spark.*

fun main(args: Array<String>) {

    val sittpuffDAO = SittpuffDao()
    val gson = Gson()

    path("/sittpuffar") {

        get("") { req, res ->
            println("API: /sittpuffar ${sittpuffDAO.sittpuffar.size} results delivered")
            res.type("application/json")
            gson.toJson(filterByTag(req, sittpuffDAO.sittpuffar))
        }

        get("/:id") { req, res ->
            res.type("application/json")
            val sittpuff: Sittpuff? = sittpuffDAO.findById(req.params("id"))
            if (sittpuff != null) {
                println("API: /sittpuffar/${req.params("id")} found")
                gson.toJson(sittpuff)
            } else {
                println("API: /sittpuffar/${req.params("id")} not found")
                res.status(404)
                "404 Not found"
            }
        }

        post("") { req, res ->
            val fromJson: Sittpuff = gson.fromJson(req.body(), Sittpuff::class.java)
            val id: String = createGuid()
            sittpuffDAO.save(fromJson.copy(id))
            println("API: /sittpuffar/${id} created")
            res.status(201)
            ""
        }

        put("/:id") { req, res ->
            val sittpuff: Sittpuff = gson.fromJson(req.body(), Sittpuff::class.java)
            println("API: /sittpuffar/${sittpuff.id} modified")
            sittpuffDAO.update(req.params("id"), sittpuff)
        }

        delete("/:id") { req, res ->
            println("API: /sittpuffar/${req.params("id")} removed")
            sittpuffDAO.delete(req.params("id"))
        }
    }
}