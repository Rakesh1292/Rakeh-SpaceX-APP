package com.example.rakesh.ui.listing.viewModel

import androidx.lifecycle.*
import com.example.rakesh.common.network.NetworkState
import com.example.rakesh.di.IoDispatcher
import com.example.rakesh.model.DataResponse
import com.example.rakesh.repository.SpaceXRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(
    private val repository: SpaceXRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _list = MutableLiveData<DataResponse?>()
    val list: LiveData<DataResponse?> = _list


    fun getData() = viewModelScope.launch(ioDispatcher) {
        _loading.postValue(true)
        when (val response = repository.getData()) {
            is NetworkState.Success -> {
                _list.postValue(response.data)
            }
            is NetworkState.Error -> {
                if (response.response.code() == 401) {
                    onError("Not authorize")
                } else {
                    onError("Something went wrong. Try later")
                }
            }
        }
        _loading.postValue(false)
    }


    private fun onError(message: String) {
        _errorMessage.postValue(message)
    }

    override fun onCleared() {
        super.onCleared()
        _errorMessage.value = null
        _loading.value = false
    }

}