package com.montoto.inventapp.data.repository

import android.app.Application
import com.montoto.inventapp.data.models.FileImageResponse
import com.montoto.inventapp.domain.model.FileImageModel
import com.montoto.inventapp.network.InventoryService
import com.montoto.inventapp.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import javax.inject.Inject

class FilesImageRepositoryImpl @Inject constructor(private val api: InventoryService, private val appContext: Application) : FileImageRepository {

    override fun getFilesImages(id: String): Flow<List<FileImageResponse>> = flow {
        val result = api.getImagesFromId(id)
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun sendImageFiles(id: String, filesList: List<FileImageModel>): Flow<String> = flow {
        val uploadRequest: MultipartUploadRequest = MultipartUploadRequest(
            appContext,
            serverUrl = Constants.Inventory.Url.FULL_URL_TO_SAVE_FILES
        )
            .setMethod(Constants.Inventory.Files.REQUEST_METHOD_SERVER)
            .addParameter(Constants.Inventory.Files.ID_PARAM, id)

        filesList.forEach {
            uploadRequest.addFileToUpload(it.path, Constants.Inventory.Files.REQUEST_PARAM_SERVER)
        }

        emit(uploadRequest.startUpload())
    }.flowOn(Dispatchers.IO)
}