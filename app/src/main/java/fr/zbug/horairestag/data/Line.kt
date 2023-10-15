package fr.zbug.horairestag.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text

@Entity(tableName = "lines")
data class Line(
    @PrimaryKey
    val id: String,
    val gtfsId: String,
    val shortName: String = "",
    val longName: String = "",
    val color: String = "",
    val textColor: String = "",
    val mode: String = "",
    val type: String = "",
) {
//    if(type == "Chrono" CircleShape else RectShape


    @SuppressLint("Range")
    @Composable
    fun getIcon() {
        var shape : Shape = RectangleShape
        if(type == "CHRONO" || type == "TRAM") {
            shape = CircleShape
        }

//        Icon(
//            Painter
//        )

        Text(
            text = shortName,
            modifier = Modifier
                .background(color = Color(android.graphics.Color.parseColor("#$color")), shape = shape )
                .padding(1.dp)
                .width(20.dp)
                .height(20.dp)
                .wrapContentSize(align = Alignment.Center),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(android.graphics.Color.parseColor("#$textColor")),
        )
    }
}