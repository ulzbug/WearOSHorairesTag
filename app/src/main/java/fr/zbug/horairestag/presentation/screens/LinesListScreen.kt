package fr.zbug.horairestag.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text


@Composable
fun LinesListScreen(
    networkId:String = "",
    onNavigateToClustersList: (String) -> Unit,
    viewModel: LinesListViewModel = viewModel(factory = LinesListViewModel.factory),
    /*...*/
) {
    viewModel.getLinesByType(networkId)
    val linesList by viewModel.lines.collectAsState()

    val scalingLazyListState = rememberScalingLazyListState()

    Scaffold(
        positionIndicator = {
            PositionIndicator(
                scalingLazyListState = scalingLazyListState
            )
        }
    ) {
        ScalingLazyColumn(
            state = scalingLazyListState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(linesList) { _, line ->
                Chip(
                    onClick = { onNavigateToClustersList(line.id) },
                    enabled = true,
                    modifier = Modifier.fillMaxSize().height(35.dp),
                    // When we have only primary label we can have up to 2 lines of text
                    label = {
                        Text(
                            text = "$networkId ${line.shortName}"
                        )
                    },
                    icon = {
                        val shortName = line.shortName.lowercase()
                        val context = LocalContext.current
                        val resource = context.resources.getIdentifier(
                            "icon_line_$shortName",
                            "drawable",
                            context.packageName
                        )
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
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun LinesListPreview() {
    LinesListScreen("Tram", {})
}