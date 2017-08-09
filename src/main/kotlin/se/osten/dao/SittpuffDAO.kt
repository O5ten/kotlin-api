package se.osten.dao

import se.osten.beans.Sittpuff
import se.osten.utils.createGuid
import spark.Request

class SittpuffDao {

    val sittpuffar = mutableListOf<Sittpuff>();

    fun save(sittpuff: Sittpuff) {
        sittpuffar.add(sittpuff)
    }

    fun findById(id: String): Sittpuff? {
        return sittpuffar.find { v -> v.id == id }
    }

    fun findByName(name: String): Sittpuff? {
        return sittpuffar.find { it.name == name }
    }




    fun update(id: String, sittpuff: Sittpuff) {
        delete(id)
        sittpuffar.add(sittpuff)
    }

    fun delete(id: String) {
        sittpuffar.removeIf { v -> v.id == id }
    }
}