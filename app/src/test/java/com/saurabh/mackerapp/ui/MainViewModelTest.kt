package com.saurabh.mackerapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.saurabh.mackerapp.dto.Plant
import com.saurabh.mackerapp.service.PlantService
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MainViewModelTest {
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
    }

    private fun givenAFeedOfMockedPlantDataAreAvailable() {
        mvm = MainViewModel()
        //plantService = PlantService()
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