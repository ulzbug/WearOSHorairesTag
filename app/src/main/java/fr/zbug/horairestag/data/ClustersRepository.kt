package fr.zbug.horairestag.data

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.json.JSONArray
import java.net.URL


class ClustersRepository(private val clusterDao: ClusterDao) {
    suspend fun getClusters(lineId: String): List<Cluster> {
        if(clusterDao.isEmpty(lineId)) {
            TagRepository().getClusters(clusterDao, lineId)
        }

        return clusterDao.getClustersByLine(lineId.uppercase())
    }

    suspend fun getCluster(id: String): Cluster = clusterDao.getCluster(id)
}

@Dao
interface ClusterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cluster: Cluster)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultiple(cluster: List<Cluster>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLineCluster(lineClusterList: List<LineCluster>)

    @Query("SELECT * from clusters WHERE code = :id")
    suspend fun getCluster(id: String): Cluster

    @Query(
        "SELECT clusters.* " +
        "FROM clusters, lines_clusters " +
        "WHERE clusters.id = lines_clusters.clusterId AND lines_clusters.lineId = :lineId " +
        "ORDER BY orderBy"
    )
    suspend fun getClustersByLine(lineId: String): List<Cluster>

    @Query("SELECT (SELECT COUNT(*) FROM lines_clusters WHERE lineId = :lineId) == 0")
    suspend fun isEmpty(lineId: String): Boolean
}