package com.montoto.inventapp.domain

import com.montoto.inventapp.data.repository.FileImageRepository
import com.montoto.inventapp.domain.mapper.toModelUnique
import com.montoto.inventapp.domain.model.FileImageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetFilesImageUseCase @Inject constructor(private val repository: FileImageRepository) {

    fun fetchFilesImage(id: String): Flow<List<FileImageModel>> = flow {
        repository.getFilesImages(id).collect{
            emit(it.map { file ->
                file.toModelUnique()
            })
        }
    }.flowOn(Dispatchers.IO)
}