package fr.zbug.horairestag.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector

data class Network(
    val name: String,
    val icone: ImageVector,
    val backgroundColor: Color,
    val textColor: Color,
    val shape: Shape,
)