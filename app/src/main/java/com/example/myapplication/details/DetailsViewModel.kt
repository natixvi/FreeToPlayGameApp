package com.example.myapplication.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.UiState
import com.example.myapplication.repository.GameRepository
import com.example.myapplication.repository.model.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel(){
    private val gameRepository = GameRepository()
    private val mutableGameData = MutableLiveData<UiState<Game>>()
    val immutableGameData: LiveData<UiState<Game>> = mutableGameData

    fun loadDetailsData(id: String){
        mutableGameData.postValue(UiState(isLoading = true))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = gameRepository.getGameDetails(id)
                if(request.isSuccessful){
                    val game = request.body()
                    Log.d("DetailsViewModel", "Received game: ${game?.description}")
                    mutableGameData.postValue(UiState(data = game))
                }else{
                    Log.e("DetailsViewModel", "Fail, code: ${request.code()}")
                    mutableGameData.postValue(UiState(error = "Fail, code: ${request.code()}"))
                }

            } catch (e: Exception) {
                Log.e("MainViewModel", "Operacja nie powiodla sie", e)
                mutableGameData.postValue(UiState(error = "Operacja nie powiodła się: ${e.message}"))
            }
        }
    }
}