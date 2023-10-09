package fr.zbug.horairestag.presentation.screens

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import fr.zbug.horairestag.HorairesTagApplication
import fr.zbug.horairestag.data.Line
import fr.zbug.horairestag.data.LinesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LinesListViewModel(
    private val linesRepository: LinesRepository,

    ) : ViewModel() {

    private val _lines = MutableStateFlow(listOf<Line>())
    val lines: StateFlow<List<Line>> = _lines.asStateFlow()

//    fun getLinesByType(networkId: String): Flow<List<Line>> = viewModelScope.launch {
//        linesRepository.getLinesByType(networkId)
//    }

    fun getLinesByType(networkId: String) {
        Log.d("LinesListViewModel", "getLinesByType Start")
        _lines.value = emptyList()
        viewModelScope.launch {
            Log.d("LinesListViewModel", "getLinesByType Start Coroutine")
            _lines.value = linesRepository.getLinesByType(networkId)
            val nbLines = lines.value?.size
            Log.d("LinesListViewModel", "getLinesByType fin Coroutine : $nbLines")
        }
    }

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HorairesTagApplication)
                val repository = LinesRepository(application.database.lineDao())
                LinesListViewModel(repository)
            }
        }
    }
}