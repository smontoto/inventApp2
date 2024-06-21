package com.montoto.inventapp.data.models

import com.google.gson.annotations.SerializedName

data class FileImageResponse(
    @SerializedName("nombre_archivo") val name: String,
    @SerializedName("ruta_archivo") val path: String,
    val type: String,
    val size: Float
)
