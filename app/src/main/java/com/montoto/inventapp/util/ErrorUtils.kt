package com.montoto.inventapp.util

import android.content.Context
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorUtils {

    /**
     * Convert an exception in a message friendly to the user.
     *
     * @param applicationContext The application context.
     * @param error The error exception.
     */
    fun errorMessage(applicationContext: Context, error: Throwable): String {
        return when (error) {
            is SocketTimeoutException -> return "Mensaje de timeout"
            is UnknownHostException -> return "Mensaje de sin conexión"
            else -> ""
        }
    }
}