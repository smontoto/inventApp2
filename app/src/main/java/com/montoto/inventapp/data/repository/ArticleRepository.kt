package com.montoto.inventapp.data.repository

import com.montoto.inventapp.data.models.ItemRequest
import com.montoto.inventapp.data.models.ItemResponse
import kotlinx.coroutines.flow.Flow

interface  ArticleRepository {
    fun getAllArticles(state: String): Flow<List<ItemResponse>>
    fun getIdForNewArticle(): Flow<String>
    fun sendNewArticle(item: ItemRequest): Flow<String>
    fun markArticleAsDelivered(id: String): Flow<String>
}