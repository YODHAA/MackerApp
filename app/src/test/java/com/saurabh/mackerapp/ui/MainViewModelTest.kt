package com.saurabh.mackerapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.saurabh.mackerapp.dto.Plant
import com.saurabh.mackerapp.service.PlantService
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock

class MainViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var mvm: MainViewModel

    @Mock
    lateinit var plantService: PlantService

    @Before
    fun setup() {
        mvm = MainViewModel()
    }

    @Test
    fun confirm_plant_species() {
        val plant: Plant = Plant("Lily ", "Karnataka", "India")
        assertEquals("India", plant.toString())
    }

    @Test
    fun searchforplant_returnplant() {
        thenResultContainPlant()
        whensearchforplant()
        givenAFeedofPlantdataAvailable()
        // thenVerifyFunctionsInvoked()

    }

    private fun thenVerifyFunctionsInvoked() {
        verify { plantService.fetchPlants("Redbud") }
        verify(exactly = 0) { plantService.fetchPlants("Maple") }
        confirmVerified(plantService)
    }

    private fun createMockData() {
        var allPlantsLiveData = MutableLiveData<ArrayList<Plant>>()
        var allPlants = ArrayList<Plant>()
        // create and add plants to our collection.
        var redbud = Plant("Cercis", "canadensis", "Eastern Redbud")
        allPlants.add(redbud)
        var redOak = Plant("Quercus", "rubra", "Red Oak")
        allPlants.add(redOak)
        var whiteOak = Plant("Quercus", "alba", "White Oak")
        allPlants.add(whiteOak)
        allPlantsLiveData.postValue(allPlants)
        every { plantService.fetchPlants(or("Redbud", "Quercus")) } returns allPlantsLiveData
        every {
            plantService.fetchPlants(
                not(
                    or(
                        "Redbud",
                        "Quercus"
                    )
                )
            )
        } returns MutableLiveData<ArrayList<Plant>>()
        mvm.plantService = plantService

    }

    private fun thenResultContainPlant() {
        mvm = MainViewModel()
        plantService = PlantService()
        createMockData()
    }

    private fun whensearchforplant() {
        mvm.fetchPlants("Redbud")

    }

    private fun givenAFeedofPlantdataAvailable() {
        var plantFound = false
        mvm.plants.observeForever {
            assertNotNull(it)
            assertTrue(it.size > 0)
            it.forEach {
                if (it.genus == "Cercis" && it.species == "canadensis" && it.common.contains("Eastern Redbud")) {
                    plantFound = true
                }
            }
        }
        assertTrue(plantFound)
    }

    @Test
    fun `search for garbage and return nothing `() {
        givenAFeedofPlantdataAvailable()
        mvm.fetchPlants("abcd")
        createMockData()
        mvm.plants.observeForever {
            assertEquals(0, it.size)
        }
    }


}