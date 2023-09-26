package fr.zbug.horairestag.presentation.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import fr.zbug.horairestag.R

@Composable
fun NetworksListScreen(
    onNavigateToLinesList: () -> Unit,
    /*...*/
) {

    val itemsIndexedList = listOf("Tram", "Chrono", "Proximo", "Flexo")

    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(itemsIndexedList) { index, item ->
            CompactChip(
                onClick = onNavigateToLinesList,
                enabled = true,
                modifier = Modifier
                    .width(140.dp),
                // When we have only primary label we can have up to 2 lines of text
                label = {
                    Text(
                        text = "$item"
                    )
                },
//                icon = {
//                    /*Text(
//                        text = item,
//                        style = TextStyle(background = Color.Yellow)
//                    )*/
//                    Image(
//                        painter = painterResource(id = R.drawable.icon_line_c1),
//                        contentDescription = "Ligne",
//                        contentScale = ContentScale.Fit,
//                        modifier = Modifier.size(ChipDefaults.IconSize)
//                            .wrapContentSize(align = Alignment.Center),
//                    )
//                }
            )
        }
    }
}