package dev.pollito.spring_kotlin.sakila.staff.domain.model

data class Staff(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val password: String,
    val email: String?,
    val active: Boolean,
)
