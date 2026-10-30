package be.vives.jarne.assignment_6.data

import be.vives.jarne.assignment_6.network.BoredApiService
import be.vives.jarne.assignment_6.network.RetrofitClient

interface BoredRepository {
    suspend fun getRandomActivity(): Result<BoredActivityDto>
}

class NetworkBoredRepository(
    private val apiService: BoredApiService = RetrofitClient.boredApiService,
) : BoredRepository {
    override suspend fun getRandomActivity(): Result<BoredActivityDto> {
        return runCatching {
            apiService.getRandomActivity()
        }
    }
}
