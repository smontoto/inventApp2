package com.montoto.inventapp.data.repository

import com.montoto.inventapp.data.models.ItemRequest
import com.montoto.inventapp.data.models.ItemResponse
import com.montoto.inventapp.network.InventoryService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ArticlesRepositoryImpl @Inject constructor(private val api: InventoryService) : ArticleRepository{

    override fun getAllArticles(state: String): Flow<List<ItemResponse>> = flow {
        val result = api.getAllArticles(state)
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun getIdForNewArticle(): Flow<String> = flow {
        val result = api.getIdForNewArticle()
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun sendNewArticle(item: ItemRequest): Flow<String> = flow{
        val result = api.addNewArticle(item.idArticle, item.article, item.clientName, item.clientCellphone, item.problemDescription)
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun markArticleAsDelivered(id: String): Flow<String> = flow {
        val result = api.markArticleAsDelivered(id)
        emit(result)
    }.flowOn(Dispatchers.IO)
}

