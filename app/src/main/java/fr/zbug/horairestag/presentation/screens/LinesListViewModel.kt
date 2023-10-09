package fr.zbug.horairestag.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import fr.zbug.horairestag.HorairesTagApplication
import fr.zbug.horairestag.data.Line
import fr.zbug.horairestag.data.LinesRepository
import kotlinx.coroutines.flow.Flow

class LinesListViewModel(
    private val linesRepository: LinesRepository,

    ) : ViewModel() {

    fun getLinesByType(networkId: String): Flow<List<Line>> = linesRepository.getLinesByType(networkId)

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