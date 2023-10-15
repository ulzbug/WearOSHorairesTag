package fr.zbug.horairestag.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.SwapVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import fr.zbug.horairestag.presentation.LoadingAnimation
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date


@Composable
fun ScheduleScreen(
    onNavigateToOtherDirection: (String, String, Int) -> Unit,
    viewModel: ScheduleViewModel = viewModel(factory = ScheduleViewModel.factory),
) {
    Scaffold(
        pageIndicator = {
        },
        timeText = {
            TimeText()
        }
    ) {
        val loaded by viewModel.loaded.collectAsState()
        val error by viewModel.error.collectAsState()

        if(!loaded) {
            LoadingAnimation()
        } else if(error != "") {
            Column(
                modifier = Modifier
                    .fillMaxSize().padding(5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = error,
                    fontSize = 12.sp
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {

                val line by viewModel.line.collectAsState()
                val cluster by viewModel.cluster.collectAsState()
                val schedules by viewModel.schedules.collectAsState()

                val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                val today = LocalDate.now().format(formatter).toInt()

                val date = Date()
                val currentDateTimeFR = date.toInstant().atZone(ZoneId.of("Europe/Paris"))
                val hours = currentDateTimeFR.format(DateTimeFormatter.ofPattern("H")).toInt()
                val minutes = currentDateTimeFR.format(DateTimeFormatter.ofPattern("m")).toInt()
                val secondes = currentDateTimeFR.format(DateTimeFormatter.ofPattern("s")).toInt()
                val currentTime = hours * 3600 + minutes * 60 + secondes

                var nextArrival = "-"
                var arrival2 = "-"
                var arrival3 = "-"
                if(schedules.size > 0) {
                    nextArrival = ((schedules[0].hour - currentTime) / 60 + (schedules[0].date.toInt() - today) * (1440 - currentTime / 60)).toString()
                }
                if(schedules.size > 1) {
                    arrival2 = ((schedules[1].hour - currentTime) / 60 + (schedules[1].date.toInt() - today) * (1440 - currentTime / 60)).toString()
                }
                if(schedules.size > 2) {
                    arrival3 = ((schedules[2].hour - currentTime) / 60 + (schedules[2].date.toInt() - today) * (1440 - currentTime / 60)).toString()
                }

                // Affichage de la ligne et du cluster affiché
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 15.dp)
                        .padding(top = 35.dp), horizontalArrangement = Arrangement.Center
                ) {
                    line.getIcon()
                    Text(cluster.name, fontSize = 12.sp, modifier = Modifier.padding(start = 2.dp))
                }

                // Affichade de la direction
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    val stopDirection by viewModel.stopDirection.collectAsState()
                    Image(
                        Icons.Rounded.ArrowForward,
                        contentDescription = "Direction",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(1.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )

                    Text(stopDirection.name, fontSize = 12.sp)
                    Image(
                        Icons.Rounded.SwapVert,
                        contentDescription = "Change Direction",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(1.dp)
                            .clickable { onNavigateToOtherDirection(line.id, cluster.code, if(schedules[0].direction == 1) 2 else 1 ) },
                        colorFilter = ColorFilter.tint(Color.White),
                    )
                }

                // Affichage du temps au milieu de l'écran
                Row(
                    Modifier
                        .height(IntrinsicSize.Max)
                        .weight(1f)
                ) {
                    Box(
                        contentAlignment = Alignment.BottomStart,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(bottom = 35.dp)
                    ) {
                        Text("*", fontSize = 20.sp)
                    }
                    Box(
                        contentAlignment = Alignment.BottomStart,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(bottom = 0.dp)
                    ) {
                        Text(nextArrival.toString(), fontSize = 60.sp)
                    }
                    Box(
                        contentAlignment = Alignment.BottomStart,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(bottom = 4.dp)
                    ) {
                        Text("min")
                    }
                }

                // Affichage des passages suivants
                Text("Passages suivants : ", fontSize = 10.sp)
                Text(
                    arrival2 + "m - " + arrival3 + "m",
                    fontSize = 10.sp,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
            }
        }
    }
}

//@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
//@Composable
//fun ScheduleScreenPreview() {
//    val database = HorairesTagApplication().database
//    ScheduleScreen(
//    viewModel = ScheduleViewModel(
//            ClustersRepository(database.clusterDao()),
//            LinesRepository(database.lineDao()),
//            StopsRepository(database.stopDao(), database.clusterDao()),
//            SavedStateHandle()
//        )
//    )
//}