package com.montoto.inventapp.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.montoto.inventapp.R
import com.montoto.inventapp.domain.*
import com.montoto.inventapp.domain.model.ErrorModel
import com.montoto.inventapp.domain.model.FileImageModel
import com.montoto.inventapp.domain.model.ItemModel
import com.montoto.inventapp.util.Constants.Inventory.ResponseStates.SUCCESS
import com.montoto.inventapp.util.Constants.Inventory.States.ITEMS_ENTERED
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCaseItem: GetArticlesUseCase,
    private val useCaseFileImage: GetFilesImageUseCase,
    private val useCaseSendItem: SendNewArticleUseCase,
    private val useCaseMarkArticleAsDelivered: MarkArticleAsDelivered,
    private val useCaseSendFilesImage: SendImageFilesUseCase,
    private val appContext: Application
): ViewModel() {

    //region data
    private val _articles = MutableLiveData<List<ItemModel>?>()
    val articles: LiveData<List<ItemModel>?> = _articles

    private val _filteredList = MutableLiveData<List<ItemModel>?>()
    val filteredList: LiveData<List<ItemModel>?> = _filteredList

    private val _idNewArticle = MutableLiveData<String>()
    val idNewArticle: LiveData<String> = _idNewArticle

    private val _filesImages = MutableLiveData<List<FileImageModel>?>()
    val filesImages: LiveData<List<FileImageModel>?> = _filesImages

    private val _itemSelected = MutableLiveData<ItemModel>()
    val itemSelected: LiveData<ItemModel> = _itemSelected
    //endregion

    //region states
    private val _articleUpdateSuccess = MutableLiveData<Boolean?>()
    val articleUpdateSuccess: LiveData<Boolean?> = _articleUpdateSuccess

    private val _itemsDelivered = MutableLiveData<Boolean?>()
    val itemsDelivered: LiveData<Boolean?> = _itemsDelivered

    private val _errorMessage = MutableLiveData<ErrorModel?>()
    val errorMessage: LiveData<ErrorModel?> = _errorMessage

    private val _errorState = MutableLiveData<Boolean?>()
    val errorState: LiveData<Boolean?> = _errorState
    //endregion

    //region screen props
    private val _toolbarTitle: MutableLiveData<String?> = MutableLiveData()
    val toolbarTitle: LiveData<String?> = _toolbarTitle

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showToast = MutableLiveData<String?>()
    val showToast: LiveData<String?> = _showToast
    //endregion

    private val _navigation = MutableLiveData<Fragment>()
    val navigation: LiveData<Fragment> = _navigation

    fun setNavigation(navigation: Fragment) {
        _navigation.postValue(navigation)
    }

    fun setToolbarTitle(title: String? = null) {
        _toolbarTitle.postValue(title)
    }

    fun resetVars(){
        _articles.postValue(null)
        _filteredList.postValue(null)
        _itemsDelivered.postValue(null)
        _showToast.postValue(null)
        _filesImages.postValue(null)
        _articleUpdateSuccess.postValue(null)
        _errorState.postValue(null)
        _errorMessage.postValue(null)
    }

    fun setItemSelected(item: ItemModel){
        _itemSelected.postValue(item)
    }

    fun setItemAsDelivered(state: Boolean){
        _itemsDelivered.postValue(state)
    }

    fun fetchItems(type: String){
        if (type == ITEMS_ENTERED){
            fetchItemEntered()
        } else {
            fetchItemDelivered()
        }
    }

    fun fetchItemEntered(){
        viewModelScope.launch {
            _isLoading.postValue(true)
            useCaseItem.fetchItemEntered()
                .catch {
                    _isLoading.postValue(false)
                    _errorState.postValue(true)
                    _errorMessage.postValue(ErrorModel("Error", "Intentar mas tarde"))
                }
                .collect {
                    _isLoading.postValue(false)
                    if (it?.isNotEmpty() == true){
                        _articles.postValue(it)
                    } else {
                        _showToast.postValue("No hay articulos")
                    }
                }
        }
    }

    private fun fetchItemDelivered(){
        viewModelScope.launch {
            _isLoading.postValue(true)
            useCaseItem.fetchItemsDelivered()
                .catch {
                    _errorMessage.postValue(ErrorModel("Error", "Intentar mas tarde"))
                    _isLoading.postValue(false)
                }
                .collect{
                    _isLoading.postValue(false)
                    if (it?.isNotEmpty() == true){
                        _articles.postValue(it)
                    } else {
                        _showToast.postValue("No hay articulos")
                    }

                }
        }
    }

    fun fetchIdForNewArticle(){
        viewModelScope.launch {
            _isLoading.postValue(true)
            useCaseItem.fetchIdForNewItem()
                .catch {
                    _errorState.postValue(true)
                    _errorMessage.postValue(ErrorModel("Error", "No se pudo obtener el ID de articulo"))
                    _isLoading.postValue(false)
                }
                .collect{
                    _isLoading.postValue(false)
                    _idNewArticle.postValue(it)
                }
        }
    }

    fun sendArticle(item: ItemModel){
        if (validateFields(item)) {
            viewModelScope.launch {
                _isLoading.postValue(true)
                useCaseSendItem.sendArticle(item)
                    .catch {
                        _errorMessage.postValue(ErrorModel("Error", "No se pudo almacenar el articulo"))
                        _isLoading.postValue(false)
                    }
                    .collect{
                        _isLoading.postValue(false)
                        if (it == SUCCESS) {
                            _showToast.postValue("Articulo subido con exito")
                            _articleUpdateSuccess.postValue(true)
                        }
                        else _showToast.postValue("Algo salió mal")
                    }
            }
        } else {
            _showToast.postValue("Faltan completar campos")
        }

    }

    @VisibleForTesting
    fun validateFields(item: ItemModel): Boolean {
        with(item){
            return this.idArticle.isNotEmpty() && this.article.isNotEmpty() && this.clientName.isNotEmpty() &&
                    this.clientCellphone.isNotEmpty() && this.problemDescription.isNotEmpty()
        }
    }

    fun markArticleAsDelivered(id: String){
        viewModelScope.launch {
            useCaseMarkArticleAsDelivered.markAsDelivered(id)
                .catch {
                    _errorMessage.postValue(ErrorModel("Error", "No se pudo actualizar el estado"))
                    _isLoading.postValue(false)
                }
                .collect{
                    if (it == SUCCESS) {
                        _showToast.postValue("Se guardó como entregado")
                        delay(1500L)
                        _articleUpdateSuccess.postValue(true)
                    }
                    else _showToast.postValue("Algo salió mal")
                }
        }
    }

    fun fetchFilesImages(id: String){
        viewModelScope.launch {
            useCaseFileImage.fetchFilesImage(id)
                .catch {
                    Log.i("TAG", "fetchFilesImages: Error imágenes")
                }
                .collect{
                    _filesImages.postValue(it)
                }
        }
    }

    fun sendFilesToServer(filesList: List<FileImageModel>, idArticle: String) {
        viewModelScope.launch {
            useCaseSendFilesImage.sendImageFiles(idArticle, filesList)
                .catch {
                    _showToast.postValue("Error enviando archivos")
                }.collect{
                    _showToast.postValue("Archivos subidos con éxito")
                }
        }
    }

    //Fixme: moverlo a un viewmodel específico del fragment que lo va a utilizar
    fun filterArticles(input: String, listItems: List<ItemModel>) {
        if (listItems.isNotEmpty()) {
            val filteredList = ArrayList<ItemModel>()
            for (item in listItems) {
                val data = (item.idArticle + item.clientName + item.article)
                if (data.lowercase(Locale.getDefault()).contains(input.lowercase(Locale.getDefault()))) {
                    filteredList.add(item)
                }
            }
            _filteredList.postValue(filteredList)
        } else {
            _showToast.postValue(appContext.getString(R.string.txt_articles_not_found))
        }
    }

    fun clearError() {
        _errorState.postValue(null)
    }

    fun clearToastMessage(){
        _showToast.postValue(null)
    }

    fun clearUpdateSuccess(){
        _articleUpdateSuccess.postValue(null)
    }

}