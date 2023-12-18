package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.model.Game
import com.example.myapplication.repository.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val gameRepository = GameRepository()

    private val mutableGamesData = MutableLiveData<UiState<List<Game>>>()
    val immutableGamesData: LiveData<UiState<List<Game>>> = mutableGamesData

   fun getData() {
       mutableGamesData.postValue(UiState(isLoading = true))
       viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = gameRepository.getGameResponse()
                if(request.isSuccessful){
                    val games = request.body()
                    mutableGamesData.postValue(UiState(data = games))
                }else{
                    mutableGamesData.postValue(UiState(error = "Fail, code: ${request.code()}"))
                }

            } catch (e: Exception) {
                mutableGamesData.postValue(UiState(error = "Operacja nie powiodła się: ${e.message}"))
                Log.e("MainViewModel", "Operacja nie powiodla sie", e)
            }
        }
    }
}