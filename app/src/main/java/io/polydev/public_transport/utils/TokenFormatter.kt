package io.polydev.public_transport.utils

object TokenFormatter {

    fun formatToken(token: String): String{
        return "Bearer $token"
    }
}