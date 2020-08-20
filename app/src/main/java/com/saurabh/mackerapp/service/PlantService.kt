package com.saurabh.mackerapp.service

import androidx.lifecycle.MutableLiveData
import com.saurabh.mackerapp.dao.IPlantDAO
import com.saurabh.mackerapp.dto.Plant
import com.saurabh.mackerapp.network.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantService {

    fun fetchPlants(plantName: String): MutableLiveData<ArrayList<Plant>> {
        var plants = MutableLiveData<ArrayList<Plant>>()
        val service = RetrofitClientInstance.retrofitInstance?.create(IPlantDAO::class.java)
        val call = service?.getAllPlants()
        call?.enqueue(object : Callback<ArrayList<Plant>> {
            override fun onFailure(call: Call<ArrayList<Plant>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ArrayList<Plant>>,
                response: Response<ArrayList<Plant>>
            ) {
                plants.value = response.body()
            }

        })

        return plants
    }
}