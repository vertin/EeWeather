package com.vertin.eetweather

import com.vertin.eetweather.db.model.PlaceStorage
import com.vertin.eetweather.db.repository.PlaceForecastStorage
import com.vertin.eetweather.db.repository.WeatherForecastStorage
import com.vertin.eetweather.domain.SummaryWeatherInteractor
import com.vertin.eetweather.domain.model.*
import com.vertin.eetweather.repository.CurrentWeatherRepository
import com.vertin.eetweather.repository.WeatherForecastRepository
import com.vertin.eetweather.util.LocationUtil
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException


class SummaryWeatherInteractorTest {


    @MockK
    lateinit var weatherForecastRepositoryMock: WeatherForecastRepository

    @MockK
    lateinit var currentWeatherRepoMock: CurrentWeatherRepository

    @MockK
    lateinit var placeLocalRepoMock: PlaceStorage

    @MockK
    lateinit var placeForecastLocalRepoMock: PlaceForecastStorage

    @MockK
    lateinit var localForecastRepoMock: WeatherForecastStorage

    @MockK
    lateinit var locationUtilMock: LocationUtil

    lateinit var interactor: SummaryWeatherInteractor

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        interactor = SummaryWeatherInteractor(
            weatherForecastRepositoryMock,
            currentWeatherRepoMock,
            placeLocalRepoMock,
            placeForecastLocalRepoMock,
            localForecastRepoMock,
            locationUtilMock
        )
    }

    @Test
    fun `load summary from network`() {
        //Given
        every { currentWeatherRepoMock.getCurrentWeather() } returns Single.just(mockk {
            every { observations } returns listOf(
                mockk {
                    every { name } returns "test_1"
                    every { phenomenon } returns "Cloudy"
                    every { latitude } returns "100"
                    every { longitude } returns "100"
                    every { airtemperature } returns "3.0"
                    every { airpressure } returns "770"
                    every { relativehumidity } returns "80"
                    every { uvindex } returns "1"
                    every { windspeed } returns "1"
                    every { winddirection } returns "90"
                }
            )
        })

        every { weatherForecastRepositoryMock.getWeatherForecast() } returns Single.just(
            listOf(
                mockk {
                    every { date } returns "2021-01-01"
                    every { day } returns mockk(relaxed = true) {
                        every { phenomenon } returns "Cloudy"
                        every { places } returns (0..3).map { mockk(relaxed = true) }
                        every { tempmin } returns 3.0
                        every { tempmax } returns 5.0
                        every { text } returns "Cloudy"
                    }
                    every { night } returns mockk(relaxed = true) {
                        every { phenomenon } returns "Clear"
                        every { places } returns (0..3).map { mockk(relaxed = true) }
                        every { tempmin } returns 2.0
                        every { tempmax } returns 3.0
                        every { text } returns "Clear"
                    }
                },
                mockk {
                    every { date } returns "2021-01-02"
                    every { day } returns mockk(relaxed = true)
                    every { night } returns mockk(relaxed = true)
                },
                mockk {
                    every { date } returns "2021-01-03"
                    every { day } returns mockk(relaxed = true)
                    every { night } returns mockk(relaxed = true)
                })
        )

        every {
            locationUtilMock.calculateDistance(
                any(),
                any(),
                any(),
                any(),
            )
        } returns 10f

        every { localForecastRepoMock.storeForecast(any()) } returns Completable.complete()
        every { placeLocalRepoMock.savePlaces(any()) } returns Completable.complete()
        every { placeForecastLocalRepoMock.savePlacesForecast(any()) } returns Completable.complete()

        val forecastStorageSlot = slot<List<ForecastPreview>>()
        val placeStorageSlot = slot<List<PlacePreview>>()
        val placeForeastSlot = slot<List<PlaceForecast>>()

        //When
        val result = interactor.getForecastWithInLocation(100.0, 100.0).test().assertNoErrors()
            .assertValueCount(1).values()

        //Then
        val summary = result[0]
        assert(summary.online)
        checkSummaryResponse(summary)

        verify(exactly = 1) { localForecastRepoMock.storeForecast(capture(forecastStorageSlot)) }
        verify(exactly = 1) { placeLocalRepoMock.savePlaces(capture(placeStorageSlot)) }
        verify(exactly = 1) { placeForecastLocalRepoMock.savePlacesForecast(capture(placeForeastSlot)) }
        verify(exactly = 0) { localForecastRepoMock.loadForecast() }
        verify(exactly = 0) { placeLocalRepoMock.loadAll() }


        assert(forecastStorageSlot.isCaptured)
        assert(placeStorageSlot.isCaptured)
        assert(placeForeastSlot.isCaptured)

        with(forecastStorageSlot.captured) {
            assertEquals(3, size)
        }
        with(placeStorageSlot.captured) {
            assertEquals(1, size)
        }
        with(placeForeastSlot.captured) {
            assertEquals(4, size)
        }

    }

    @Test
    fun `load summary without network, using cache`() {
        //Given
        every { currentWeatherRepoMock.getCurrentWeather() } returns Single.error(IOException("Server not found"))
        every { weatherForecastRepositoryMock.getWeatherForecast() } returns Single.error(
            IOException("Server not found")
        )
        every {
            locationUtilMock.calculateDistance(
                any(),
                any(),
                any(),
                any(),
            )
        } returns 10f

        every { localForecastRepoMock.loadForecast() } returns Single.just(listOf(mockk {
            every { date } returns "2021-01-01"
            every { day } returns mockk(relaxed = true) {
                every { phenomenon } returns Phenomenon.CLOUDY
                every { tempMin } returns 3
                every { tempMax } returns 5
                every { description } returns "Cloudy"
            }
            every { night } returns mockk(relaxed = true) {
                every { phenomenon } returns Phenomenon.CLEAR
                every { tempMin } returns 2
                every { tempMax } returns 3
                every { description } returns "Clear"
            }
        }, mockk {
            every { date } returns "2021-01-02"
            every { day } returns mockk(relaxed = true)
            every { night } returns mockk(relaxed = true)
        }, mockk {
            every { date } returns "2021-01-03"
            every { day } returns mockk(relaxed = true)
            every { night } returns mockk(relaxed = true)
        }))
        every { placeLocalRepoMock.loadAll() } returns Single.just(listOf(mockk {
            every { temperature } returns "1"
            every { phenomenon } returns Phenomenon.CLOUDY
            every { wind } returns mockk {
                every { speed } returns "1"
                every { direction } returns "90"
            }
            every { latitude } returns 100.0
            every { longitude } returns 100.0
            every { airpressure } returns "700"
            every { name } returns "test_1"
        }))

        //When
        val result = interactor.getForecastWithInLocation(100.0, 100.0).test().assertNoErrors()
            .assertValueCount(1).values()

        //Then
        val summary = result[0]
        assertFalse(summary.online)

        verify(exactly = 0) { localForecastRepoMock.storeForecast(any()) }
        verify(exactly = 0) { placeLocalRepoMock.savePlaces(any()) }
        verify(exactly = 0) { placeForecastLocalRepoMock.savePlacesForecast(any()) }
        verify(exactly = 1) { localForecastRepoMock.loadForecast() }
        verify(exactly = 1) { placeLocalRepoMock.loadAll() }

        verify(exactly = 1) { locationUtilMock.calculateDistance(any(), any(), any(), any()) }


    }


    private fun checkSummaryResponse(summary: Summary) {
        with(summary.placeWeather) {
            assertEquals("test_1", name)
            assertEquals(Phenomenon.CLOUDY, phenomenon)
            assertEquals("3.0", temperature)
            assertNotNull(wind)
            assertEquals("90", wind!!.direction)
            assertEquals("1", wind!!.speed)
            assertEquals("1", uvindex)
        }
        with(summary.forecast) {
            assertEquals(3, this.size)
            val today = get(0)
            assertEquals("2021-01-01", today.date)
            assertEquals(3, today.day.tempMin)
            assertEquals(5, today.day.tempMax)
            assertEquals(2, today.night.tempMin)
            assertEquals(3, today.night.tempMax)
            assertEquals(Phenomenon.CLOUDY, today.day.phenomenon)
            assertEquals(Phenomenon.CLEAR, today.night.phenomenon)
        }
    }

    @Test
    fun `try to load summary, not network, no cache`() {
        //Given
        every { currentWeatherRepoMock.getCurrentWeather() } returns Single.error(IOException("Server not found"))
        every { weatherForecastRepositoryMock.getWeatherForecast() } returns Single.error(
            IOException("Server not found")
        )
        every { localForecastRepoMock.loadForecast() } returns Single.just(emptyList())
        every { placeLocalRepoMock.loadAll() } returns Single.just(emptyList())

        //When
        val result = interactor.getForecastWithInLocation(100.0, 100.0).test().assertError(IOException::class.java)

        //Then
        verify(exactly = 0) { localForecastRepoMock.storeForecast(any()) }
        verify(exactly = 0) { placeLocalRepoMock.savePlaces(any()) }
        verify(exactly = 0) { placeForecastLocalRepoMock.savePlacesForecast(any()) }
        verify(exactly = 1) { placeLocalRepoMock.loadAll() }


    }


}