package se.osten

import se.osten.beans.DatabaseConnection
import se.osten.dao.MongoEntityDAO
import se.osten.paths.EntityPath
import se.osten.paths.Filters
import se.osten.utils.encode64
import spark.ModelAndView
import spark.Spark
import spark.Spark.get
import spark.Spark.path
import spark.kotlin.port
import spark.template.handlebars.HandlebarsTemplateEngine
import java.io.File
import java.util.*

fun main(args: Array<String>) {

    val properties = Properties();
    val file = File(".api-config");
    val fis = file.inputStream()
    val handlebars = HandlebarsTemplateEngine()

    properties.load(fis)
    fis.close()

    val dbConn = DatabaseConnection(properties)
    val users = properties.getProperty("api_users").split(',').map { encode64(it) }

    port(properties.getProperty("exposePort").toInt())
    Filters(users).enableAuthentication()

    println("Registering resources:\n================")
    path("/skribentus"){
        val resources = properties.getProperty("paths").split(',')
        val daos = resources.map { path ->
            val entityDao = MongoEntityDAO(dbConn, path);
            EntityPath(entityDao, users).enableResource(path)
            println(path)
            entityDao
        }
        get(""){ req, res ->
            val map: Map<String, Any> = hashMapOf("resources" to resources, "entities" to daos.map { it.count() } )
            val page: String = handlebars.render(
                    ModelAndView(map, "api.html")
            )
            page
        }
    }


    println("================\nSkribentus API Started at http://localhost:4567/skribentus")
}