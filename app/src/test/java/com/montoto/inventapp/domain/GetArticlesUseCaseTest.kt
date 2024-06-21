package com.montoto.inventapp.domain

import com.montoto.inventapp.BaseUnitTest
import com.montoto.inventapp.data.models.ItemResponse
import com.montoto.inventapp.data.repository.ArticleRepository
import com.montoto.inventapp.domain.mapper.toModelUnique
import com.montoto.inventapp.domain.model.ItemModel
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
internal class GetArticlesUseCaseTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var articlesRepository: ArticleRepository

    lateinit var useCaseItem: GetArticlesUseCase

    override fun setup() {
        super.setup()
        useCaseItem = GetArticlesUseCase(articlesRepository)
    }

    @Test
    fun `when The Api Not Return Anything Data From BackEnd`() = runTest {

        useCaseItem.fetchItemEntered().collect()

        verify(exactly = 1) { articlesRepository.getAllArticles(any()) }
    }

    @Test
    fun `when The Api Return Something then get values from api`() = runBlocking {
        //Given
        val itemResponseList = listOf(ItemResponse("1", "TV", "Roto", "22", "", "Pedro", "384123123"))
        val itemResponseModel = itemResponseList.map { it.toModelUnique() }

        val flow = flowOf(itemResponseList)
        every { articlesRepository.getAllArticles(any()) } returns flow

        //When
        var result = listOf<ItemModel>()
        useCaseItem.fetchItemEntered().collect{
            result = it!!
        }

        //Then
        verify{ articlesRepository.getAllArticles(any()) }
        Assert.assertEquals(result, itemResponseModel)

    }
}