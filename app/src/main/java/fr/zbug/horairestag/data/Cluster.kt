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
    val lat: Double = 0.0,
    val lon: Double = 0.0
)

@Entity(primaryKeys = ["lineId", "clusterId"], tableName = "lines_clusters")
data class LineCluster(
    val lineId: String,
    val clusterId: String,
    var orderBy:Int
)

//data class LinesWithClusters(
//    @Embedded val line: Line,
//    @Relation(
//        parentColumn = "gtfsId",
//        entityColumn = "id",
//        associateBy = Junction(LineClusterCrossRef::class)
//    )
//    val clusters: List<Cluster>
//)