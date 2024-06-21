package com.montoto.inventapp.domain.mapper

import com.montoto.inventapp.data.models.FileImageResponse
import com.montoto.inventapp.data.models.ItemResponse
import com.montoto.inventapp.domain.model.FileImageModel
import com.montoto.inventapp.domain.model.ItemModel

fun ItemResponse.toModelUnique(): ItemModel {
    return ItemModel(
        idArticle = idArticle,
        article = article,
        problemDescription = problemDescription,
        startDate = startDate,
        clientName = clientName,
        clientCellphone = clientCellphone)
}

fun FileImageResponse.toModelUnique(): FileImageModel {
    return FileImageModel(
        name = name,
        path = path)
}


