package fr.zbug.horairestag.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

@Composable
fun ClustersListScreen(
    lineId:String = "",
    viewModel: ClustersListViewModel = viewModel(factory = ClustersListViewModel.factory),
    /*...*/
) {

    Log.d("ClusterListScreen", "Récupération des clusters")

    val clusterList by viewModel.getClusters(lineId).collectAsState(emptyList())

    Log.d("ClusterListScreen", clusterList.toList().toString())

//    val clusterList : List<Cluster> = listOf(
//        Cluster( id = "SEM:LETOILE", name = "L'Etoile"),
//        Cluster( id = "SEM:CHANDON", name = "Edmée Chandon"),
//        Cluster( id = "SEM:DENPAPIN", name = "Denis Papin"),
//        Cluster( id = "SEM:ADELAUNE", name = "Auguste Delaune"),
//        Cluster( id = "SEM:MARIECUR", name = "Marie Curie"),
//    )

    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(clusterList) { _, cluster ->
            CompactChip(
                onClick = { /* Do something */ },
                enabled = true,
                modifier = Modifier
                    .width(140.dp),
                // When we have only primary label we can have up to 2 lines of text
                label = {
                    Text(
                        text = cluster.name
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