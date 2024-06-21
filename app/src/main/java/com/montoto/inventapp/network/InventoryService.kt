package com.montoto.inventapp.network

import com.montoto.inventapp.data.models.FileImageResponse
import com.montoto.inventapp.data.models.ItemResponse
import retrofit2.http.*

interface InventoryService {

    /**
     * Method used to obtain a list of items that can be delivered or entered according param "state"
     * @param state
     * @return List of items
     */
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("test_app/inventapp/get_articulos.php")
    suspend fun getAllArticles(@Field("estado") state: String): List<ItemResponse>

    /**
     * Method used to obtain a list of path images by id
     * @param id
     * @return List of path images
     */
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("test_app/inventapp/get_files_from_id.php")
    suspend fun getImagesFromId(@Field("id_articulo") id: String): List<FileImageResponse>

    /**
     * Method used to obtain an new Id for add a new article
     * @return id string
     */
    @Headers("Accept: application/json")
    @POST("test_app/inventapp/get_new_id.php")
    suspend fun getIdForNewArticle(): String

    //FIXME: refactorizar backend para recibir un objeto.
    /**
     * Method used to add a new article on the backend
     * @param id id that return "get_new_id.php" service
     * @param item detail of article
     * @param clientName name of client
     * @param clientPhone contact number of client
     * @param problemDescription description of the problem to be repaired
     * @return state: success o error from backend
     */
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("test_app/inventapp/add_new_article.php")
    suspend fun addNewArticle(
        @Field("id_articulo") id: String,
        @Field("articulo") item: String,
        @Field("nombre_cliente") clientName: String,
        @Field("telefono") clientPhone: String,
        @Field("desc_problema") problemDescription: String): String

    /**
     * Method used to change the state of article, from entered to delivered, on the backend
     * @param id
     * @return state: success o error from backend
     */
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("test_app/inventapp/marcar_articulo_entregado.php")
    suspend fun markArticleAsDelivered(@Field("id_articulo") id: String): String
}