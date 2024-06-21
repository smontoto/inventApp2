package com.montoto.inventapp.domain

import com.montoto.inventapp.data.repository.FileImageRepository
import com.montoto.inventapp.domain.model.FileImageModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendImageFilesUseCase @Inject constructor(private val repository: FileImageRepository) {

    fun sendImageFiles(id: String, filesImages: List<FileImageModel>): Flow<String> =
        repository.sendImageFiles(id, filesImages)
}