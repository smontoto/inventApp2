package com.montoto.inventapp.data.models

import com.google.gson.annotations.SerializedName

data class ItemResponse(
    @SerializedName("id_articulo") val idArticle: String,
    @SerializedName("articulo") val article: String,
    @SerializedName("descripcion_problema") val problemDescription: String,
    @SerializedName("precio_reparacion") val repairPrice: String? = null,
    @SerializedName("fecha_alta") val startDate: String? = null,
    @SerializedName("nombre_cliente") val clientName: String,
    @SerializedName("telefono_cliente") val clientCellphone: String
)
