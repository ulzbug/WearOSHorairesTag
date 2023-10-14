package fr.zbug.horairestag.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import fr.zbug.horairestag.presentation.LoadingAnimation
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date


@Composable
fun ScheduleScreen(
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
        val line by viewModel.line.collectAsState()
        val cluster by viewModel.cluster.collectAsState()
        val schedules by viewModel.schedules.collectAsState()

        val date = Date()
        val currentDateTimeFR = date.toInstant().atZone(ZoneId.of("Europe/Paris"))
        val hours = currentDateTimeFR.format(DateTimeFormatter.ofPattern("H")).toInt()
        val minutes = currentDateTimeFR.format(DateTimeFormatter.ofPattern("m")).toInt()
        val secondes = currentDateTimeFR.format(DateTimeFormatter.ofPattern("s")).toInt()
        val currentTime = hours * 3600 + minutes * 60 + secondes

        var NextArrival = "-"
        var arrival2 = "-"
        var arrival3 = "-"
        if(schedules.size > 0) {
            NextArrival = ((schedules[0].hour - currentTime) / 60).toString()
        }
        if(schedules.size > 0) {
            arrival2 = ((schedules[1].hour - currentTime) / 60).toString()
        }
        if(schedules.size > 0) {
            arrival3 = ((schedules[2].hour - currentTime) / 60).toString()
        }

        if(!loaded) {
            LoadingAnimation()
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .padding(top = 35.dp), horizontalArrangement = Arrangement.Center
                ) {
                    Box(Modifier.width(30.dp)) {
                        Text(
                            text = line.shortName,
                            modifier = Modifier
                                .background(color = Color.Yellow, shape = CircleShape)
                                .padding(4.dp),
                            color = Color.Black,
                        )
                    }
                    Box(
                        Modifier
                            .width(110.dp)
                            .padding(start = 2.dp)
                    ) {
                        Text(cluster.name, fontSize = 13.sp)
                    }
                }
                Row(
                    Modifier
                        .height(IntrinsicSize.Max)
                        .weight(1f)
                        .padding(bottom = 20.dp)
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
                        Text(NextArrival.toString(), fontSize = 60.sp)
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

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun ScheduleScreenPreview() {
    ScheduleScreen()
}