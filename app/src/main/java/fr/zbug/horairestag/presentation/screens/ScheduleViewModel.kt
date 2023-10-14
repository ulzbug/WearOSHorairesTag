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
import fr.zbug.horairestag.data.LinesRepository
import fr.zbug.horairestag.data.Schedule
import fr.zbug.horairestag.data.StopsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScheduleViewModel (
    private val clustersRepository: ClustersRepository,
    private val linesRepository: LinesRepository,
    private val stopsRepository: StopsRepository
    ) : ViewModel()
    {
        private val _line = MutableStateFlow(Line("", ""))
        val line: StateFlow<Line> = _line.asStateFlow()

        private val _cluster = MutableStateFlow(Cluster("", ""))
        val cluster: StateFlow<Cluster> = _cluster.asStateFlow()

        private val _schedules = MutableStateFlow(ArrayList<Schedule>())
        val schedules: StateFlow<ArrayList<Schedule>> = _schedules.asStateFlow()

        fun getLine(lineId: String) {
            viewModelScope.launch {
                _line.value = linesRepository.getLine(lineId)
            }
        }

        fun getCluster(clusterId: String) {
            viewModelScope.launch {
                val cluster = clustersRepository.getCluster(clusterId)
                if(cluster != null) {
                    _cluster.value = cluster
                }
            }
        }

        fun getSchedules(lineId: String, clusterId: String, direction: Int) {
            viewModelScope.launch {
                val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                val date = LocalDate.now().format(formatter)
                _schedules.value = stopsRepository.getSchedules(lineId, clusterId, date, direction)
            }
        }

        companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HorairesTagApplication)
                val clustersRepository = ClustersRepository(application.database.clusterDao())
                val linesRepository = LinesRepository(application.database.lineDao())
                val stopsRepository = StopsRepository(application.database.stopDao(), application.database.clusterDao())
                ScheduleViewModel(clustersRepository, linesRepository, stopsRepository)
            }
        }
    }
}
