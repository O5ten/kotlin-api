package se.osten.paths

import com.google.gson.Gson
import se.osten.api.SittpuffDAO
import se.osten.beans.Entity
import se.osten.beans.Sittpuff
import se.osten.utils.*
import spark.ModelAndView
import spark.Spark.*
import spark.kotlin.get
import spark.template.handlebars.HandlebarsTemplateEngine
import java.util.*

class SittpuffPath(val dao: SittpuffDAO, val properties: Properties) {

    val gson = Gson()
    val handlebars = HandlebarsTemplateEngine()
    val authenticatedUsers = properties.getProperty("sittpuff_users").split(',').map { encode64(it) }

    fun activate() {

        before("/*") { req, res ->
            val authHeader = req.headers("Authorization");
            if (authHeader != null) {
                val token = authHeader.split(" ")[1];
                if(token !in authenticatedUsers){
                    halt(401, "You are not a valid user")
                }
            }else{
                res.header("WWW-Authenticate", "Basic realm=\"User Visible Realm\"")
                halt(401, "authenticate yourself")
            }
        }

        path("/sittpuffar") {

            get("/new") { req, res ->
                val map: Map<String, Any> = emptyMap<String, Any>()
                val page: String = handlebars.render(
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
                    val page: String = handlebars.render(
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

                val page: String = handlebars.render(
                        ModelAndView(map, "list.html")
                )
                page
            }

            get("") { req, res ->
                res.type("application/json")
                val entities = filterByTag(req, dao.findAll())
                log(req, " ${entities.size} results delivered")
                gson.toJson(entities)
            }

            get("/:id") { req, res ->
                res.type("application/json")
                val id = req.params("id")
                val sittpuff: Sittpuff? = dao.findById(id)
                if (sittpuff != null) {
                    log(req, "$id found")
                    gson.toJson(sittpuff)
                } else {
                    log(req, "$id not found")
                    res.status(404)
                    gson.toJson(Entity(id, "not found"))
                }
            }

            post("") { req, res ->
                val fromJson: Sittpuff = gson.fromJson(req.body(), Sittpuff::class.java)
                val id: String = createGuid()
                dao.save(fromJson.copy(id))
                log(req, "${id} created")
                res.status(201)
                res.type("application/json")
                gson.toJson(Entity(id, "created"))
            }

            put("/:id") { req, res ->
                val id = req.params("id")
                res.type("application/json")
                val sittpuff: Sittpuff = gson.fromJson(req.body(), Sittpuff::class.java)
                log(req, "${id} modified")
                dao.update(id, sittpuff)
                gson.toJson(Entity(id, "modified"))
            }

            delete("/:id") { req, res ->
                val id = req.params("id")
                res.type("application/json")
                log(req, "${id} removed")
                dao.delete(id)
                gson.toJson(Entity(id, "removed"))
            }
        }
    }
}