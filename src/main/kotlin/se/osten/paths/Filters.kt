package se.osten.paths

import spark.Spark

class Filters(private val users: List<String>) {

    fun enableAuthentication() {
        Spark.before("/*") { req, res ->
            val authHeader = req.headers("Authorization");
            if (authHeader != null) {
                val token = authHeader.split(" ")[1];
                if (token !in users) {
                    Spark.halt(401, "You are not a valid user")
                }
            } else {
                res.header("WWW-Authenticate", "Basic realm=\"User Visible Realm\"")
                Spark.halt(401, "authenticate yourself")
            }
        }
    }
}