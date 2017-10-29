package se.osten.paths

import com.google.gson.Gson
import se.osten.api.DAO
import se.osten.beans.Entity
import se.osten.beans.SlimEntity
import se.osten.utils.createGuid
import se.osten.utils.filterByTag
import se.osten.utils.log
import se.osten.utils.toHashMap
import spark.ModelAndView
import spark.Spark
import spark.Spark.*
import spark.template.handlebars.HandlebarsTemplateEngine
import java.util.*

class EntityPath(private val dao: DAO<Entity>, private val users: List<String>) {

    val gson = Gson()
    val handlebars = HandlebarsTemplateEngine()

    fun enableResource(path: String) {

        path("/$path") {

            get("/new") { req, res ->
                val map: Map<String, Any> = hashMapOf("path" to path)
                val page: String = handlebars.render(
                        ModelAndView(map, "new.html")
                )
                page
            }

            get("/:id/edit") { req, res ->
                val id: String = req.params("id")
                val entity: Entity? = dao.findById(id)
                val justSaved = req.queryParams().contains("justSaved");
                if (entity != null) {
                    val map: HashMap<String, Any> = toHashMap(entity)
                    map.put("justSaved", justSaved)
                    map.put("path", path)
                    val page: String = handlebars.render(
                            ModelAndView(map, "edit.html")
                    )
                    page
                } else {
                    res.status(404)
                    res.type("application/json")
                    gson.toJson(SlimEntity(id, "not found"))
                }
            }

            get("/edit") { req, res ->
                val entities: List<Map<String, Any>> = dao.findAll().map { toHashMap(it) }
                val map: Map<String, Any> = hashMapOf("entities" to entities, "path" to path)
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
                val sittpuff: Entity? = dao.findById(id)
                if (sittpuff != null) {
                    log(req, "$id found")
                    gson.toJson(sittpuff)
                } else {
                    log(req, "$id not found")
                    res.status(404)
                    gson.toJson(SlimEntity(id, "not found"))
                }
            }

            post("") { req, res ->
                val fromJson: Entity = gson.fromJson(req.body(), Entity::class.java)
                val id: String = createGuid()
                dao.save(fromJson.copy(id))
                log(req, " created")
                res.status(201)
                res.type("application/json")
                gson.toJson(SlimEntity(id, "created"))
            }

            put("/:id") { req, res ->
                val id = req.params("id")
                res.type("application/json")
                val sittpuff: Entity = gson.fromJson(req.body(), Entity::class.java)
                log(req, " modified")
                dao.update(id, sittpuff)
                gson.toJson(SlimEntity(id, "modified"))
            }

            delete("/:id") { req, res ->
                val id = req.params("id")
                res.type("application/json")
                log(req, " removed")
                dao.delete(id)
                gson.toJson(SlimEntity(id, "removed"))
            }
        }
    }
}