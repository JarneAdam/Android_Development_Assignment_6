package be.vives.jarne.assignment_6.network

import be.vives.jarne.assignment_6.data.BoredActivityDto
import retrofit2.http.GET

interface BoredApiService {
    @GET("random")
    suspend fun getRandomActivity(): BoredActivityDto
}
