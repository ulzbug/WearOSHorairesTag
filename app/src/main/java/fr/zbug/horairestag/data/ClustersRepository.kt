package fr.zbug.horairestag.data

import android.os.StrictMode
import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import java.net.URL


class ClustersRepository(private val clusterDao: ClusterDao) {
    suspend fun getClusters(lineId: String): List<Cluster> {
        if(clusterDao.isEmpty(lineId)) {
            Log.d("ClustersRepository", "URL : https://data.mobilites-m.fr/api/routers/default/index/routes/$lineId/clusters")

            val repoListJsonStr = URL("https://data.mobilites-m.fr/api/routers/default/index/routes/$lineId/clusters").readText()

            val jsonObj = JSONArray(repoListJsonStr)

            var linesCluster = ArrayList<LineCluster>()

            for (i in 0 until jsonObj.length()) {
                val jsonCluster = jsonObj.getJSONObject(i)
                val clusterId = jsonCluster.getString("id")
                val cluster = Cluster(
                    id = clusterId,
                    code = jsonCluster.getString("code"),
                    city = jsonCluster.getString("city"),
                    name = jsonCluster.getString("name"),
                    visible = jsonCluster.getString("visible") == "true",
                    lat = jsonCluster.getDouble("lat"),
                    lon = jsonCluster.getDouble("lon")
                )
                clusterDao.insert(cluster)

                val lineCluster = LineCluster(
                    lineId = lineId,
                    clusterId = clusterId,
                    orderBy = i,
                )
                linesCluster.add(lineCluster)
            }
            clusterDao.insertLineCluster(linesCluster)
        }

        return clusterDao.getClusters2(lineId.uppercase())
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
    suspend fun getClusters2(lineId: String): List<Cluster>

    @Query("SELECT (SELECT COUNT(*) FROM lines_clusters WHERE lineId = :lineId) == 0")
    suspend fun isEmpty(lineId: String): Boolean
}