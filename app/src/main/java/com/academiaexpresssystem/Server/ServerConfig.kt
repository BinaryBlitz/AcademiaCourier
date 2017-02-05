package com.academiaexpresssystem.Server

object ServerConfig {
    val baseUrl = "http://academia-delivery.herokuapp.com"
    val apiURL = baseUrl + "/courier/"

    val imageUrl: String
        get() {
            return ""
        }

    val prefsName = "AcademiaSystemPrefs"
    val tokenEntity = "auth_token"
    val userIdEntity = "user_id"
}
