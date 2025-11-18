package student.projects.innerspace.api

import retrofit2.Response
import retrofit2.http.*
import student.projects.innerspace.data.Note

interface ApiService {

    @POST("api/auth/register")
    suspend fun registerUser(@Body user: Map<String, String>): Response<Unit>

    @POST("api/auth/login")
    suspend fun loginUser(@Body credentials: Map<String, String>): Response<Map<String, String>> // JWT token

    @GET("api/entries")
    suspend fun getEntries(): Response<List<Note>>

    @POST("api/entries")
    suspend fun createEntry(@Body note: Note): Response<Unit>

    @PUT("api/entries/{id}")
    suspend fun updateEntry(@Path("id") id: Int, @Body note: Note): Response<Unit>

    @DELETE("api/entries/{id}")
    suspend fun deleteEntry(@Path("id") id: Int): Response<Unit>
}
