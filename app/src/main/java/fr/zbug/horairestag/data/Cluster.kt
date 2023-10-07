package fr.zbug.horairestag.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clusters")
data class Cluster(
    @PrimaryKey
    val id: String,
    val code: String = "",
    val city: String = "",
    val name: String = "",
    val visible: Boolean = true,
    val lat: Float = 0F,
    val lon: Float = 0F
) {


}