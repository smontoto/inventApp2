package com.montoto.inventapp.data.repository

import com.montoto.inventapp.data.models.FileImageResponse
import com.montoto.inventapp.domain.model.FileImageModel
import kotlinx.coroutines.flow.Flow

interface FileImageRepository {
    fun getFilesImages(id: String): Flow<List<FileImageResponse>>
    fun sendImageFiles(id: String, filesList: List<FileImageModel>): Flow<String>
}