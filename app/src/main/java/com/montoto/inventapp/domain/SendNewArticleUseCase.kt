package com.montoto.inventapp.domain

import com.montoto.inventapp.data.models.ItemRequest
import com.montoto.inventapp.data.repository.ArticleRepository
import com.montoto.inventapp.domain.model.ItemModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendNewArticleUseCase @Inject constructor(private val repository: ArticleRepository) {

    fun sendArticle(item: ItemModel): Flow<String> =
        repository.sendNewArticle(
            ItemRequest(
                idArticle = item.idArticle,
                article = item.article,
                problemDescription = item.problemDescription,
                clientName = item.clientName,
                clientCellphone = item.clientCellphone
            )
        )
}