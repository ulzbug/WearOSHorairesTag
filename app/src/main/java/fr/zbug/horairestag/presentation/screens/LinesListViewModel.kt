package fr.zbug.horairestag.presentation.screens

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import fr.zbug.horairestag.HorairesTagApplication
import fr.zbug.horairestag.data.Line
import fr.zbug.horairestag.data.LinesRepository
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LinesListViewModel(
    private val linesRepository: LinesRepository,
    private val savedStateHandle: SavedStateHandle
    ) : ViewModel() {

    private val networkId = checkNotNull(savedStateHandle.get<String>("networkId"))
    private val _lines = MutableStateFlow(listOf<Line>())
    val lines: StateFlow<List<Line>> = _lines.asStateFlow()

    init {
        Log.d("LinesListViewModel", "init")
        this.getLinesByType(networkId)
    }

    private fun getLinesByType(networkId: String) {
        if(_lines.value.isEmpty()) {
            Log.d("LinesListViewModel", "getLinesByType Start")
            viewModelScope.launch {
                Log.d("LinesListViewModel", "getLinesByType Start Coroutine")
                _lines.value = linesRepository.getLinesByType(networkId)
                val nbLines = lines.value?.size
                Log.d("LinesListViewModel", "getLinesByType fin Coroutine : $nbLines")
            }
        }
    }

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HorairesTagApplication)
                val repository = LinesRepository(application.database.lineDao())
                val savedStateHandle = createSavedStateHandle()

                LinesListViewModel(repository, savedStateHandle)
            }
        }
    }
}