package fr.zbug.horairestag.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text

@Composable
fun ClustersListScreen(
    lineId:String = "",
    onNavigateToSchedule: (String, String) -> Unit,
    viewModel: ClustersListViewModel = viewModel(factory = ClustersListViewModel.factory),
    /*...*/
) {
    viewModel.getClusters(lineId)
    val clusterList by viewModel.clusters.collectAsState()

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
        ) {
            itemsIndexed(clusterList) { _, cluster ->
                Chip(
                    onClick = { onNavigateToSchedule(lineId, cluster.code) },
                    enabled = true,
                    modifier = Modifier.fillMaxSize().height(35.dp),
                    // When we have only primary label we can have up to 2 lines of text
                    label = {
                        Text(
                            text = cluster.name,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    },
                )
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun ClustersListPreview() {
    ClustersListScreen("SEM:A", fun(_: String, _: String) { })
}