package fr.zbug.horairestag.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stops")
data class Stop(
    @PrimaryKey
    val gtfsId: String,
    val name: String = "",
    val city: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val clusterGtfsId: String = ""
)
