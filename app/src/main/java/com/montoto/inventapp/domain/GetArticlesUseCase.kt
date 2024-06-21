package com.montoto.inventapp.domain

import com.montoto.inventapp.data.repository.ArticleRepository
import com.montoto.inventapp.domain.mapper.toModelUnique
import com.montoto.inventapp.domain.model.ItemModel
import com.montoto.inventapp.util.Constants.Inventory.States.ITEMS_DELIVERED
import com.montoto.inventapp.util.Constants.Inventory.States.ITEMS_ENTERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor(private val repository: ArticleRepository){

    fun fetchItemEntered(): Flow<List<ItemModel>?> {
        return flow {
            repository.getAllArticles(ITEMS_ENTERED).collect {
                emit(it.map { item ->
                    item.toModelUnique()
                })
            }
        }
    }

    fun fetchItemsDelivered(): Flow<List<ItemModel>?> = flow {
        repository.getAllArticles(ITEMS_DELIVERED).collect {
            emit(it.map { item ->
                item.toModelUnique()
            })
        }
    }

    fun fetchIdForNewItem(): Flow<String> = repository.getIdForNewArticle()

}