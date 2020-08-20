package com.saurabh.mackerapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.saurabh.mackerapp.dto.Plant
import com.saurabh.mackerapp.service.PlantService
import com.saurabh.mackerapp.ui.MainViewModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class PlantDataIntegrationTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var mvm: MainViewModel


    var plantService = mockk<PlantService>()

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
        givenAFeedOfMockedPlantDataAreAvailable()
        whenSearchForPlant()
        thenResultContainsPlant()
        thenVerifyFunctionsInvoked()

    }

    private fun thenVerifyFunctionsInvoked() {
        // fetchPlants is called with Param Redbud
        verify { plantService.fetchPlants("Redbud") }
        // fetchPlants is never called with Param Maple
        verify(exactly = 0) { plantService.fetchPlants("Maple") }
        confirmVerified(plantService)
    }

    private fun givenAFeedOfMockedPlantDataAreAvailable() {
        mvm = MainViewModel()
        //plantService = PlantService()
        createMockData()
    }

    private fun createMockData() {
        val allPlantsLiveData = MutableLiveData<ArrayList<Plant>>()
        val allPlants = ArrayList<Plant>()
        // create and add plants to our collection.
        val redbud = Plant("Cercis", "canadensis", "Eastern Redbud")
        allPlants.add(redbud)
        val redOak = Plant("Quercus", "rubra", "Red Oak")
        allPlants.add(redOak)
        val whiteOak = Plant("Quercus", "alba", "White Oak")
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


    private fun whenSearchForPlant() {
        mvm.fetchPlants("Redbud")

    }

    private fun thenResultContainsPlant() {
        var plantFound = false
        mvm.plants.observeForever {
            assertNotNull(it)
            assertTrue(it.size > 0)
            it.forEach {
                if (it.genus == "Cercis" && it.species == "canadensis" && it.common.contains("Eastern Redbud")) {
                    plantFound = true
                }
            }
            assertTrue(plantFound)
        }
    }

    @Test
    fun `search for garbage and return nothing `() {
        //givenAFeedOfMockedPlantDataAreAvailable()
        mvm = MainViewModel()
        mvm.fetchPlants("abcd")
        mvm.plants.observeForever {
            assertEquals(0, it.size)
        }
    }


}