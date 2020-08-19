package com.saurabh.mackerapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saurabh.mackerapp.dto.Plant
import com.saurabh.mackerapp.service.PlantService

class MainViewModel : ViewModel() {

    var plants: MutableLiveData<ArrayList<Plant>> = MutableLiveData<ArrayList<Plant>>()
    var plantService: PlantService = PlantService()

    fun fetchPlants(plantName: String) {
        plants = plantService.fetchPlants(plantName)
    }
}