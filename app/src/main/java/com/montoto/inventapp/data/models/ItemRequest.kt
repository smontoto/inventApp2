package com.montoto.inventapp.data.models

data class ItemRequest(
    val idArticle: String,
    val article: String,
    val problemDescription: String,
    val clientName: String,
    val clientCellphone: String
)