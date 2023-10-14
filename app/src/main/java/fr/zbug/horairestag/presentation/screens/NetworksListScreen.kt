package fr.zbug.horairestag.presentation.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.Tram
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import fr.zbug.horairestag.data.Network

@Composable
fun NetworksListScreen(
    onNavigateToLinesList: (String) -> Unit,
    /*...*/
) {
    val itemsIndexedList = listOf(
        Network("Tram", Icons.Rounded.Tram, Color(0x33, 0x76, 0xB8, 255), Color.White, CircleShape),
        Network("Chrono", Icons.Rounded.DirectionsBus, Color.Yellow, Color.Black, CircleShape),
        Network("Proximo", Icons.Rounded.DirectionsBus, Color(0x33, 0xA4, 0x57, 255), Color.White, RectangleShape),
        Network("Flexo", Icons.Rounded.DirectionsBus, Color(0xD5, 0x72, 0xA8, 255), Color.White, RectangleShape),
    )

    Log.d("NetworksListScreen", "Création du NetworksListScreen")

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
            modifier = Modifier.background(MaterialTheme.colors.background),
        ) {
            itemsIndexed(itemsIndexedList) { _, network ->
                Chip(
                    onClick = { onNavigateToLinesList(network.name) },
                    enabled = true,
                    modifier = Modifier.fillMaxSize().height(35.dp),
                    // When we have only primary label we can have up to 2 lines of text
                    label = {
                        Text(
                            text = network.name
                        )
                    },
                    icon = {
                        Image(
                            network.icone,
                            contentDescription = network.name,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(ChipDefaults.IconSize)
                                .wrapContentSize(align = Alignment.Center)
                                .background(color = network.backgroundColor, shape = network.shape)
                                .padding(4.dp),
                            colorFilter = ColorFilter.tint(network.textColor)
                        )
                    },
                    contentPadding = PaddingValues(horizontal = 20.dp),
                )
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun NetworksListPreview() {
    NetworksListScreen {}
}