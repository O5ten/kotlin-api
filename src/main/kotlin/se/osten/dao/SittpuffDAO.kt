package se.osten.dao

import se.osten.beans.Sittpuff
import se.osten.utils.createGuid

class SittpuffDao {

    val sittpuffar = HashMap<String, Sittpuff>()

    fun save(sittpuff: Sittpuff) {
        sittpuffar[sittpuff.id] = sittpuff
    }

    fun findById(id: String): Sittpuff? {
        return sittpuffar[id]
    }

    fun findByName(name: String): Sittpuff? {
        return sittpuffar.values.find { it.name == name}
    }

    fun findByTag(tag: String): List<Sittpuff>? {
        return sittpuffar.values.filter { v -> tag in v.tags }
    }

    fun update(id: String, sittpuff: Sittpuff) {
        sittpuffar[id] = sittpuff
    }

    fun delete(id: String) {
        sittpuffar.remove(id)
    }
}