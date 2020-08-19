package com.saurabh.mackerapp.service

import androidx.lifecycle.MutableLiveData
import com.saurabh.mackerapp.dto.Plant

class PlantService {

    fun fetchPlants(plantName: String): MutableLiveData<ArrayList<Plant>> {
        return MutableLiveData<ArrayList<Plant>>()
    }
}