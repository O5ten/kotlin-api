package se.osten

import com.google.gson.Gson
import se.osten.beans.Entity
import se.osten.beans.Sittpuff
import se.osten.dao.MongoSittpuffDAO
import se.osten.utils.createGuid
import se.osten.utils.filterByTag
import se.osten.utils.toHashMap
import spark.ModelAndView
import spark.Request
import spark.Spark.*
import spark.template.handlebars.HandlebarsTemplateEngine

fun main(args: Array<String>) {

    val dao = MongoSittpuffDAO()
    val gson = Gson()

    fun log(req: Request, event: String = "") {
        println("API: ${req.ip()} ${req.requestMethod()} /sittpuffar/$event")
    }

    port(4567)
    path("/sittpuffar") {

        get("/new") { req, res ->
            val map: Map<String, Any> = emptyMap<String, Any>()
            val page: String = HandlebarsTemplateEngine().render(
                    ModelAndView(map, "new.html")
            )
            page
        }

        get("/:id/edit") { req, res ->
            val id: String = req.params("id")
            val puff: Sittpuff? = dao.findById(id)
            val justSaved = req.queryParams().contains("justSaved");
            if (puff != null) {
                val map: HashMap<String, Any> = toHashMap(puff)
                map.put("justSaved", justSaved)
                val page: String = HandlebarsTemplateEngine().render(
                        ModelAndView(map, "edit.html")
                )
                page
            } else {
                res.status(404)
                res.type("application/json")
                gson.toJson(Entity(id, "not found"))
            }
        }

        get("/edit") { req, res ->
            val sittpuffar: List<Map<String, Any>> = dao.findAll().map { toHashMap(it) }
            val map: Map<String, Any> = hashMapOf("sittpuffar" to sittpuffar)
            val page: String = HandlebarsTemplateEngine().render(
                    ModelAndView(map, "list.html")
            )
            page
        }

        get("") { req, res ->
            log(req, "${dao.count()} results delivered")
            res.type("application/json")
            gson.toJson(filterByTag(req, dao.findAll()))
        }

        get("/:id") { req, res ->
            res.type("application/json")
            val id = req.params("id")
            val sittpuff: Sittpuff? = dao.findById(id)
            if (sittpuff != null) {
                log(req, "/$id found")
                gson.toJson(sittpuff)
            } else {
                log(req, "/$id not found")
                res.status(404)
                gson.toJson(Entity(id, "not found"))
            }
        }

        post("") { req, res ->
            val fromJson: Sittpuff = gson.fromJson(req.body(), Sittpuff::class.java)
            val id: String = createGuid()
            dao.save(fromJson.copy(id))
            log(req, "/${id} created")
            res.status(201)
            res.type("application/json")
            gson.toJson(Entity(id, "created"))
        }

        put("/:id") { req, res ->
            val id = req.params("id")
            res.type("application/json")
            val sittpuff: Sittpuff = gson.fromJson(req.body(), Sittpuff::class.java)
            log(req, "/${id} modified")
            dao.update(id, sittpuff)
            gson.toJson(Entity(id, "modified"))

        }

        delete("/:id") { req, res ->
            val id = req.params("id")
            res.type("application/json")
            log(req, "/${id} removed")
            dao.delete(id)
            gson.toJson(Entity(id, "removed"))
        }
    }
}