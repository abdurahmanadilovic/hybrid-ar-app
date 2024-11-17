package com.abdu.hybridapp.data

import com.abdu.hybridapp.domain.Position3d
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NodesRepositoryTest {
    private lateinit var mockApiService: MockApiService
    private lateinit var repository: NodesRepositoryImpl

    private class MockApiService : ApiService {
        var positionToReturn = LocationDTO(Location(0f, 0f, 0f))
        var callCount = 0

        override suspend fun getInitialWorldPosition(): LocationDTO {
            callCount++
            return positionToReturn
        }
    }

    @Before
    fun setup() {
        mockApiService = MockApiService()
        repository = NodesRepositoryImpl(mockApiService)
    }

    @Test
    fun `getInitialPosition returns correct position from API`() = runBlocking {
        // Given
        mockApiService.positionToReturn = LocationDTO(Location(1.0f, 2.0f, 3.0f))

        // When
        val result = repository.getInitialPosition()

        // Then
        assertEquals(Position3d(1.0f, 2.0f, 3.0f), result)
        assertEquals(1, mockApiService.callCount)
    }

    @Test
    fun `getInitialPosition caches and returns same position on subsequent calls`() = runBlocking {
        // Given
        mockApiService.positionToReturn = LocationDTO(Location(1.0f, 2.0f, 3.0f))

        // When
        val firstResult = repository.getInitialPosition()
        val secondResult = repository.getInitialPosition()

        // Then
        assertEquals(firstResult, secondResult)
        assertEquals(1, mockApiService.callCount)
    }
}