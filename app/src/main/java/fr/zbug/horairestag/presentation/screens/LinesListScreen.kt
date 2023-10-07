package fr.zbug.horairestag.presentation.screens

import android.app.Application
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import fr.zbug.horairestag.HorairesTagApplication
import fr.zbug.horairestag.data.Line


@Composable
fun LinesListScreen(
    networkId:String = "",
    viewModel: LinesListViewModel = viewModel(factory = LinesListViewModel.factory),
    /*...*/
) {

    val linesList by viewModel.getLinesByType(networkId).collectAsState(emptyList())

//    var itemsIndexedList : List<Line> = listOf(
//        Line( id = 1, gtfsId = "SEM:1", shortName = "C1"),
//        Line( id = 2, gtfsId = "SEM:2", shortName = "C2"),
//        Line( id = 3, gtfsId = "SEM:3", shortName = "C3"),
//        Line( id = 4, gtfsId = "SEM:4", shortName = "C4"),
//        Line( id = 5, gtfsId = "SEM:5", shortName = "C5"),
//    )

    Log.d("Pouet", linesList.toList().toString());

    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(linesList) { _, line ->
            CompactChip(
                onClick = { /* Do something */ },
                enabled = true,
                modifier = Modifier
                    .width(140.dp),
                // When we have only primary label we can have up to 2 lines of text
                label = {
                    Text(
                        text = "$networkId ${line.shortName}"
                    )
                },
                icon = {
                    val shortName = line.shortName.lowercase()
                    val context = LocalContext.current
                    val resource = context.resources.getIdentifier("icon_line_$shortName", "drawable", context.packageName)
                    Image(
                        painter = painterResource(id = resource),
                        contentDescription = "Ligne",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(ChipDefaults.IconSize)
                            .wrapContentSize(align = Alignment.Center),
                    )
                }
            )
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun LinesListPreview() {
    LinesListScreen("Tram")
}