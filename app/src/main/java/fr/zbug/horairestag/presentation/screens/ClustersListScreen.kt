package fr.zbug.horairestag.presentation.screens

import android.util.Log
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
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

@Composable
fun ClustersListScreen(
    lineId:String = "",
    viewModel: ClustersListViewModel = viewModel(factory = ClustersListViewModel.factory),
    /*...*/
) {
    val clusterList by viewModel.getClusters(lineId).collectAsState(emptyList())

    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ) {
        itemsIndexed(clusterList) { _, cluster ->
            Chip(
                onClick = { /* Do something */ },
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

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun ClustersListPreview() {
    ClustersListScreen("SEM:A")
}