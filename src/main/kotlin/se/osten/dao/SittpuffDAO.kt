package se.osten.dao

import se.osten.beans.Sittpuff

interface SittpuffDAO {
    fun save(sittpuff: Sittpuff)
    fun findById(id: String): Sittpuff?
    fun findByName(name: String): Sittpuff?
    fun findAll(): List<Sittpuff>
    fun update(id: String, sittpuff: Sittpuff)
    fun delete(id: String)
    fun count(): Long
}