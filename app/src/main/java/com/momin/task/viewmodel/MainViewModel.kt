package com.momin.task.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.momin.task.repository.DatabaseRepository
import com.momin.task.repository.NetworkRepository
import com.momin.task.models.Data
import com.momin.task.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val dbRepository: DatabaseRepository
) : ViewModel() {

    private val _state = MutableLiveData<Resource<ArrayList<Data>>>()
    val state: LiveData<Resource<ArrayList<Data>>> = _state
    private val query = MutableLiveData("")

    /**
     * This is the live data which we are populating on recyclerview.
     * Getting cities from local DB using PagingData.
    * we are using switchMap to get either all or filtered data based on query string*/
    val offlineCitiesList = query.switchMap { queryString ->
        if (queryString.isEmpty()) {
            dbRepository.getAllCities().cachedIn(viewModelScope)
        } else {
            dbRepository.getFilteredCities(queryString).cachedIn(viewModelScope)
        }
    }

    /**
     * Getting Json file from remote server and saving it in local db so
     * that we can use PagingAdapter and search easily.
     * Getting from server only if local db is empty
     */
    fun getCitiesListFromRemoteSource() {
        viewModelScope.launch {
            withContext(IO) {
                if (dbRepository.isDbEmpty()) {
                    _state.postValue(Resource.LOADING())
                    val response = networkRepository.getData()
                    val dataHandler = handleResponse(response)
                    saveInLocalDb(dataHandler.data)
                    _state.postValue(dataHandler)
                }
            }
        }
    }

    private suspend fun saveInLocalDb(data: ArrayList<Data>?) {
        data?.let { dbRepository.insertAll(it) }
    }

    /**
     * As we are getting .gz file in response, we are extracting Json from .gz file in GzInterceptor class
     * and here we are converting it to our data class so that we can save it in DB.
     */
    private fun handleResponse(response: Response<ResponseBody>): Resource<ArrayList<Data>> {
        if (response.isSuccessful) {
            response.body()?.let { it ->
                val gson = Gson()
                val type = object : TypeToken<ArrayList<Data>>() {}.type
                val body = it.string()
                return Resource.SUCCESS(gson.fromJson(body, type))
            }
        }
        return Resource.ERROR(message = response.errorBody().toString())
    }

    /**
     * Assigning value to query livedata will invoke the switchMap to get filtered data
     */
    fun searchCityInDb(query: String) {
        this.query.value = query
    }
}

