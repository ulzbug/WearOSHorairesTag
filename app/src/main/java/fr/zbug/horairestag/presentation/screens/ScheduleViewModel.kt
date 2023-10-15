package fr.zbug.horairestag.presentation.screens

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import fr.zbug.horairestag.HorairesTagApplication
import fr.zbug.horairestag.data.Cluster
import fr.zbug.horairestag.data.ClustersRepository
import fr.zbug.horairestag.data.Line
import fr.zbug.horairestag.data.LinesRepository
import fr.zbug.horairestag.data.Schedule
import fr.zbug.horairestag.data.Stop
import fr.zbug.horairestag.data.StopsRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScheduleViewModel (
    private val clustersRepository: ClustersRepository,
    private val linesRepository: LinesRepository,
    private val stopsRepository: StopsRepository,
    savedStateHandle: SavedStateHandle
    ) : ViewModel()
    {
        private val lineId = checkNotNull(savedStateHandle.get<String>("lineId"))
        private val clusterId = checkNotNull(savedStateHandle.get<String>("clusterId"))
        private val direction = checkNotNull(savedStateHandle.get<Int>("direction"))

        private val _loaded = MutableStateFlow(false)
        val loaded: StateFlow<Boolean> = _loaded.asStateFlow()

        private val _line = MutableStateFlow(Line("", ""))
        val line: StateFlow<Line> = _line.asStateFlow()

        private val _cluster = MutableStateFlow(Cluster("", ""))
        val cluster: StateFlow<Cluster> = _cluster.asStateFlow()

        private val _schedules = MutableStateFlow(ArrayList<Schedule>())
        val schedules: StateFlow<ArrayList<Schedule>> = _schedules.asStateFlow()

        private val _stopDirection = MutableStateFlow(Stop(""))
        val stopDirection: StateFlow<Stop> = _stopDirection.asStateFlow()

        private val _error = MutableStateFlow("")
        val error: StateFlow<String> = _error.asStateFlow()

        init {
            viewModelScope.launch {
                coroutineScope {
                    launch {
                        Log.d("ScheduleViewModel", "getLine")
                        _line.value = linesRepository.getLine(lineId)
                        Log.d("ScheduleViewModel", "getLine end")
                    }
                    launch {
                        Log.d("ScheduleViewModel", "getCluster")
                        _cluster.value = clustersRepository.getCluster(clusterId)
                        Log.d("ScheduleViewModel", "getCluster end")
                    }
                    launch {
                        Log.d("ScheduleViewModel", "getSchedules")
                        try {
                            _schedules.value =
                                stopsRepository.getSchedules(lineId, clusterId, direction)
                            if (schedules.value.size > 0) {
                                Log.d("ScheduleViewModel", schedules.value[0].stopEndId)
                                val stop = stopsRepository.getStop(schedules.value[0].stopEndId)
                                if(stop != null) {
                                    _stopDirection.value = stop
                                }
                            }
                        } catch (e : Exception) {
                            if(e.message != null) {
                                _error.value = checkNotNull(e.message)
                            }
                        }
                        Log.d("ScheduleViewModel", "getSchedules end")
                    }
                }
                _loaded.value = true
            }
            Log.d("ScheduleViewModel", "init end")
        }

        companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HorairesTagApplication)
                val clustersRepository = ClustersRepository(application.database.clusterDao())
                val linesRepository = LinesRepository(application.database.lineDao())
                val stopsRepository = StopsRepository(application.database.stopDao(), application.database.clusterDao())
                val savedStateHandle = createSavedStateHandle()

                ScheduleViewModel(clustersRepository, linesRepository, stopsRepository, savedStateHandle)
            }
        }
    }
}
