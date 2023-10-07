package fr.zbug.horairestag.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lines")
data class Line(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val gtfsId: String,
    val shortName: String = "",
    val longName: String = "",
    val color: String = "",
    val textColor: String = "",
    val mode: String = "",
    val type: String = "",
) {


}