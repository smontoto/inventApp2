package com.montoto.inventapp.domain

import com.montoto.inventapp.data.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MarkArticleAsDelivered @Inject constructor(private val repository: ArticleRepository){

    fun markAsDelivered(id: String): Flow<String> = repository.markArticleAsDelivered(id)
}