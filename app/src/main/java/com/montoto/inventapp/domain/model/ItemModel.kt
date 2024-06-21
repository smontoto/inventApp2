package com.montoto.inventapp.domain.model

data class ItemModel(
    val idArticle: String,
    val article: String,
    val problemDescription: String,
    val startDate: String? = null,
    val clientName: String,
    val clientCellphone: String
)