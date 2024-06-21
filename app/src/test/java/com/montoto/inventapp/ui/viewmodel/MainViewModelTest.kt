package com.montoto.inventapp.ui.viewmodel

import androidx.lifecycle.Observer
import com.montoto.inventapp.BaseUnitTest
import com.montoto.inventapp.domain.GetArticlesUseCase
import com.montoto.inventapp.domain.GetFilesImageUseCase
import com.montoto.inventapp.domain.MarkArticleAsDelivered
import com.montoto.inventapp.domain.SendImageFilesUseCase
import com.montoto.inventapp.domain.SendNewArticleUseCase
import com.montoto.inventapp.domain.model.ErrorModel
import com.montoto.inventapp.domain.model.ItemModel
import com.montoto.inventapp.util.Constants.Inventory.ResponseStates.ERROR
import com.montoto.inventapp.util.Constants.Inventory.ResponseStates.SUCCESS
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

internal class MainViewModelTest : BaseUnitTest() {

    @RelaxedMockK
    lateinit var useCaseItem: GetArticlesUseCase

    @RelaxedMockK
    lateinit var useCaseFileImage: GetFilesImageUseCase

    @RelaxedMockK
    lateinit var useCaseSendItem: SendNewArticleUseCase

    @RelaxedMockK
    lateinit var useCaseMarkArticleAsDelivered: MarkArticleAsDelivered

    @RelaxedMockK
    lateinit var useCaseSendFilesImage: SendImageFilesUseCase

    lateinit var viewModel: MainViewModel

    override fun setup() {
        super.setup()

        viewModel = MainViewModel(
            useCaseItem,
            useCaseFileImage,
            useCaseSendItem,
            useCaseMarkArticleAsDelivered,
            useCaseSendFilesImage,
            mockApplication
        )
    }

    @Test
    fun `When fetchItemEntered is called and the response is success, verify loading post value and useCase is called and articles post value`() {

        val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)

        viewModel.isLoading.observeForever(loadingObserver)

        viewModel.fetchItemEntered()

        verify { loadingObserver.onChanged(true) }
        verify { useCaseItem.fetchItemEntered() }

        viewModel.isLoading.removeObserver(loadingObserver)

    }

    @Test
    fun `When fetchItemEntered is called and the response fail, verify error state and error message is called`() =
        runBlocking {
            // Given
            val expectedError = ErrorModel("Error", "Intentar mas tarde")
            val observerErrorState: Observer<Boolean?> = mockk(relaxed = true)
            val observerErrorMessage: Observer<ErrorModel?> = mockk(relaxed = true)

            viewModel.errorState.observeForever(observerErrorState)
            viewModel.errorMessage.observeForever(observerErrorMessage)

            coEvery { useCaseItem.fetchItemEntered() } returns flow {
                throw Exception("Test exception")
            }

            //When
            viewModel.fetchItemEntered()

            //Then
            Assert.assertEquals(false, viewModel.isLoading.value)
            verify {
                observerErrorMessage.onChanged(expectedError)
                observerErrorState.onChanged(true)
            }

            viewModel.errorState.removeObserver(observerErrorState)
            viewModel.errorMessage.removeObserver(observerErrorMessage)

        }

    @Test
    fun `When fetchItemEntered is called sets articles and isLoading correctly`() = runBlocking {
        val articles = mockk<ArrayList<ItemModel>>(relaxed = true)
        //val articles = listOf(ItemModel(idArticle = "1", article = "Test", clientName = "Seba Montoto", clientCellphone = "1234567890", problemDescription = "Test problem"))
        val flowList = flowOf(articles)
        every { useCaseItem.fetchItemEntered() } returns flowList

        val articlesObserver: Observer<List<ItemModel>?> = mockk(relaxed = true)
        val loadingObserver: Observer<Boolean> = mockk(relaxed = true)

        viewModel.articles.observeForever(articlesObserver)
        viewModel.isLoading.observeForever(loadingObserver)

        viewModel.fetchItemEntered()

        verify { loadingObserver.onChanged(true) }
        verify { articlesObserver.onChanged(articles) }
        verify { loadingObserver.onChanged(false) }

        viewModel.articles.removeObserver(articlesObserver)
        viewModel.isLoading.removeObserver(loadingObserver)
    }

    @Test
    fun `When fetchItemEntered is called and list isEmpty show message`() = runBlocking {
        val message = "No hay articulos"
        val articlesList = flowOf(emptyList<ItemModel>())
        every { useCaseItem.fetchItemEntered() } returns articlesList

        val loadingObserver: Observer<Boolean> = mockk(relaxed = true)
        val toastObserver: Observer<String?> = mockk(relaxed = true)

        viewModel.isLoading.observeForever(loadingObserver)
        viewModel.showToast.observeForever(toastObserver)

        viewModel.fetchItemEntered()

        Assert.assertEquals(message, viewModel.showToast.value)
        verify { loadingObserver.onChanged(true) }
        verify { loadingObserver.onChanged(false) }
        verify { toastObserver.onChanged(any()) }

        viewModel.showToast.removeObserver(toastObserver)
        viewModel.isLoading.removeObserver(loadingObserver)
    }

    @Test
    fun `When fetchIdForNewArticle is called and the response fail, verify error state and error message is called`() =
        runBlocking {
            // Given
            val expectedError = mockk<ErrorModel>(relaxed = true)
            val observerErrorState: Observer<Boolean?> = mockk(relaxed = true)
            val observerErrorMessage: Observer<ErrorModel?> = mockk(relaxed = true)

            viewModel.errorState.observeForever(observerErrorState)
            viewModel.errorMessage.observeForever(observerErrorMessage)

            coEvery { useCaseItem.fetchIdForNewItem() } returns flow {
                throw Exception("Test exception")
            }

            //When
            viewModel.fetchIdForNewArticle()

            //Then
            Assert.assertEquals(false, viewModel.isLoading.value)
            verify {
                observerErrorMessage.onChanged(any())
                observerErrorState.onChanged(true)
            }

            viewModel.errorState.removeObserver(observerErrorState)
            viewModel.errorMessage.removeObserver(observerErrorMessage)
        }

    @Test
    fun `when fields are valid, send article successfully`() = runBlocking {
        val item = ItemModel(
            idArticle = "1",
            article = "Televisor",
            clientName = "Seba Montoto",
            clientCellphone = "1234567890",
            problemDescription = "Falla plaqueta"
        )

        val itemRelaxedMockK = mockk<ItemModel>(relaxed = true)

        //every { viewModel.validateFields(item) } returns true
        coEvery { useCaseSendItem.sendArticle(item) } returns flow { emit(SUCCESS) }

        val observerLoading: Observer<Boolean> = mockk(relaxed = true)
        val observerToast: Observer<String?> = mockk(relaxed = true)
        val observerSuccess: Observer<Boolean?> = mockk(relaxed = true)

        viewModel.isLoading.observeForever(observerLoading)
        viewModel.showToast.observeForever(observerToast)
        viewModel.articleUpdateSuccess.observeForever(observerSuccess)

        viewModel.sendArticle(item)

        coVerify { observerLoading.onChanged(true) }
        coVerify { observerLoading.onChanged(false) }
        coVerify { observerToast.onChanged("Articulo subido con exito") }
        coVerify { observerSuccess.onChanged(true) }

        viewModel.isLoading.removeObserver(observerLoading)
        viewModel.showToast.removeObserver(observerToast)
        viewModel.articleUpdateSuccess.removeObserver(observerSuccess)
    }

    @Test
    fun `when fields are valid, but server not response with SUCCESS`() = runBlocking {
        val item = ItemModel(
            idArticle = "1",
            article = "Televisor",
            clientName = "Seba Montoto",
            clientCellphone = "1234567890",
            problemDescription = "Falla plaqueta"
        )

        val itemRelaxedMockK = mockk<ItemModel>(relaxed = true)

        coEvery { useCaseSendItem.sendArticle(item) } returns flow { emit(ERROR) }

        val observerLoading: Observer<Boolean> = mockk(relaxed = true)
        val observerToast: Observer<String?> = mockk(relaxed = true)
        val observerSuccess: Observer<Boolean?> = mockk(relaxed = true)

        viewModel.isLoading.observeForever(observerLoading)
        viewModel.showToast.observeForever(observerToast)
        viewModel.articleUpdateSuccess.observeForever(observerSuccess)

        viewModel.sendArticle(item)

        coVerify { observerLoading.onChanged(true) }
        coVerify { observerLoading.onChanged(false) }
        coVerify { observerToast.onChanged("Algo salió mal") }
        Assert.assertNotEquals(true, viewModel.articleUpdateSuccess.value)

        viewModel.isLoading.removeObserver(observerLoading)
        viewModel.showToast.removeObserver(observerToast)
        viewModel.articleUpdateSuccess.removeObserver(observerSuccess)
    }

    @Test
    fun `when fields are invalid, show toast message`() = runBlocking {
        //Se incluye spyk para probar el método validateFields(), de otra manera da error
        val viewModel: MainViewModel = spyk(
            MainViewModel(
                useCaseItem,
                useCaseFileImage,
                useCaseSendItem,
                useCaseMarkArticleAsDelivered,
                useCaseSendFilesImage,
                mockApplication
            )
        )

        val item = ItemModel(
            idArticle = "1",
            article = "Sample Article",
            clientName = "Seba Montoto",
            clientCellphone = "1234567890",
            problemDescription = "Sample Problem"
        )

        every { viewModel.validateFields(item) } returns false

        val observerToast: Observer<String?> = mockk(relaxed = true)
        viewModel.showToast.observeForever(observerToast)

        viewModel.sendArticle(item)

        verify { observerToast.onChanged("Faltan completar campos") }

        viewModel.showToast.removeObserver(observerToast)
    }
}