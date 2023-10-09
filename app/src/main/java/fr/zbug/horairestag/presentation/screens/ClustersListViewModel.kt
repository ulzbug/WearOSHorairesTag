package fr.zbug.horairestag.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import fr.zbug.horairestag.HorairesTagApplication
import fr.zbug.horairestag.data.Cluster
import fr.zbug.horairestag.data.ClustersRepository
import kotlinx.coroutines.flow.Flow

class ClustersListViewModel (
    private val clustersRepository: ClustersRepository

    ) : ViewModel()
    {

        fun getClusters(lineId: String): Flow<List<Cluster>> = clustersRepository.getClusters(lineId)

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
