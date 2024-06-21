package com.montoto.inventapp.data.repository

import com.montoto.inventapp.BaseUnitTest
import com.montoto.inventapp.data.models.ItemResponse
import com.montoto.inventapp.network.InventoryService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
internal class ArticlesRepositoryImplTest : BaseUnitTest() {

    @MockK
    lateinit var api: InventoryService
    lateinit var repositoryImpl: ArticleRepository

    override fun setup() {
        super.setup()

        repositoryImpl = ArticlesRepositoryImpl(api)
    }

    @Test
    fun `When getArticles is called, verify the api method is called`() =
        runBlocking {
            val list = mockk<List<ItemResponse>>(relaxed = true)

            coEvery { api.getAllArticles("string") } returns list

            repositoryImpl.getAllArticles("string").collect()

            coVerify(exactly = 1) { api.getAllArticles("string") }
        }

    @Test
    fun `When getIdForNewArticle is called, verify api method is called and the result be a number`() = runTest {
        val expectedType = "22"

        coEvery { api.getIdForNewArticle() } returns expectedType

        var result: String? = null
        repositoryImpl.getIdForNewArticle().collect {result = it}

        coVerify (exactly = 1){ api.getIdForNewArticle() }
        assertTrue("The result should be a number", result?.matches(Regex("\\d+")) ?: false)
    }
}