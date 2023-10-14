package fr.zbug.horairestag.presentation.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import fr.zbug.horairestag.HorairesTagApplication
import fr.zbug.horairestag.data.Cluster
import fr.zbug.horairestag.data.ClustersRepository
import fr.zbug.horairestag.data.Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClustersListViewModel (
    private val clustersRepository: ClustersRepository

    ) : ViewModel()
    {
        private val _clusters = MutableStateFlow(listOf<Cluster>())
        val clusters: StateFlow<List<Cluster>> = _clusters.asStateFlow()

        fun getClusters(lineId: String) {
            _clusters.value = emptyList()
            viewModelScope.launch {
                _clusters.value = clustersRepository.getClusters(lineId)
            }
        }

        companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HorairesTagApplication)
                val repository = ClustersRepository(application.database.clusterDao())
                ClustersListViewModel(repository)
            }
        }
    }
}
