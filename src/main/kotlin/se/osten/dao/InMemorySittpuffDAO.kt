package se.osten.dao

import se.osten.beans.Sittpuff

class InMemorySittpuffDAO : SittpuffDAO {


    val sittpuffar = mutableListOf<Sittpuff>();

    override fun count(): Long {
        return sittpuffar.size.toLong()
    }

    override fun findAll(): List<Sittpuff> {
        return sittpuffar
    }

    override fun save(sittpuff: Sittpuff) {
        sittpuffar.add(sittpuff)
    }

    override fun findById(id: String): Sittpuff? {
        return sittpuffar.find { v -> v.id == id }
    }

    override fun findByName(name: String): Sittpuff? {
        return sittpuffar.find { it.name == name }
    }

    override fun update(id: String, sittpuff: Sittpuff) {
        delete(id)
        sittpuffar.add(sittpuff)
    }

    override fun delete(id: String) {
        sittpuffar.removeIf { v -> v.id == id }
    }
}