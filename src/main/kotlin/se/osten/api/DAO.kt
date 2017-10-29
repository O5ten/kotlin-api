package se.osten.api

interface DAO<T> {
    fun save(sittpuff: T)
    fun findById(id: String): T?
    fun findByName(name: String): T?
    fun findAll(): List<T>
    fun update(id: String, sittpuff: T)
    fun delete(id: String)
    fun count(): Long
}