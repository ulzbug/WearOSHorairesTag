package fr.zbug.horairestag.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["lineId", "clusterId", "date"])], tableName = "schedules")
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lineId: String = "",
    val clusterId: String = "",
    val direction: Int = 0,
    val stopEndId: String = "",
    val date: String = "",
    val hour: Int = 0
)
