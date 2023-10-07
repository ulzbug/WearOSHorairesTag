package fr.zbug.horairestag.presentation.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import fr.zbug.horairestag.HorairesTagApplication
import fr.zbug.horairestag.data.HorairesTagDatabase
import fr.zbug.horairestag.data.HorairesTagDatabase.Companion.getDatabase
import fr.zbug.horairestag.data.Line
import fr.zbug.horairestag.data.LineDao
import fr.zbug.horairestag.data.LinesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException

class LinesListViewModel(
    private val linesRepository: LinesRepository,

) : ViewModel() {

    fun getLinesByType(networkId: String): Flow<List<Line>> =
        linesRepository.getLinesByType(networkId)

    private var itemsIndexedList: List<Line> = listOf(
        Line( id = 1, gtfsId = "SEM:1", shortName = "C1"),
        Line( id = 2, gtfsId = "SEM:2", shortName = "C2"),
        Line( id = 3, gtfsId = "SEM:3", shortName = "C3"),
        Line( id = 4, gtfsId = "SEM:4", shortName = "C4"),
        Line( id = 5, gtfsId = "SEM:5", shortName = "C5"),
    )

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