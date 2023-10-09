package fr.zbug.horairestag.data

import android.os.StrictMode
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL


class StopsRepository(private val stopDao: StopDao, private val clusterDao: ClusterDao) {
    fun createStops() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        if(stopDao.isStopsEmpty()) {
            val repoListJsonStr = URL("https://data.mobilites-m.fr/api/points/json?types=stops%2Cclusters").readText()
            val jsonObj = JSONObject(repoListJsonStr).getJSONArray("features")

            val clusters = ArrayList<Cluster>()
            val stops = ArrayList<Stop>()

            for (i in 0 until jsonObj.length()) {
                val jsonCluster = jsonObj.getJSONObject(i)
                val properties = jsonCluster.getJSONObject("properties")
                val coords = jsonCluster.getJSONObject("geometry").getJSONArray("coordinates")
                if(properties.getString("type") == "clusters") {
                    val cluster = Cluster(
                        id = properties.getString("id"),
                        code = properties.getString("code"),
                        city = properties.getString("city"),
                        name = properties.getString("name"),
                        visible = properties.getString("visible") == "true",
                        lat = coords.getDouble(0),
                        lon = coords.getDouble(1)
                    )
                    clusters.add(cluster)

                    if(clusters.size > 1000) {
                        clusterDao.insertMultiple(clusters)
                        clusters.clear()
                    }

                } else if(properties.getString("type") == "stops") {
                    val stop = Stop(
                        gtfsId = properties.getString("id"),
                        city = properties.getString("city"),
                        name = properties.getString("name"),
                        lat = coords.getDouble(0),
                        lon = coords.getDouble(1),
                        clusterGtfsId = properties.getString("clusterGtfsId")
                    )
                    stops.add(stop)

                    if(stops.size > 1000) {
                        stopDao.insertMultipleStops(stops)
                        stops.clear()
                    }
                }

                clusterDao.insertMultiple(clusters)
                stopDao.insertMultipleStops(stops)
            }
        }
    }

    fun getSchedules(clusterId:String, date:String, lineId:String) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val schedules = ArrayList<Schedule>()

        if(stopDao.isSchedulesEmpty(clusterId, date, lineId)) {
            val repoListJsonStr = URL("https://data.mobilites-m.fr/api/routers/default/index/clusters/$clusterId/stoptimes/$date?route=$lineId").readText()
            val patterns = JSONArray(repoListJsonStr)
            for (i in 0 until patterns.length()) {
                val pattern = patterns.getJSONObject(i).getJSONObject("pattern");
                val times = patterns.getJSONObject(i).getJSONArray("times");

                for (j in 0 until times.length()) {
                    val time = times.getJSONObject(i);

                    schedules.add(
                        Schedule(
                            lineId,
                            clusterId,
                            pattern.getInt("dir"),
                            pattern.getString("lastStop"),
                            date,
                            time.getInt("scheduledArrival")
                        )
                    )

                    if(schedules.size > 1000) {
                        stopDao.insertMultipleSchedule(schedules)
                        schedules.clear()
                    }
                }
            }
        }
        stopDao.insertMultipleSchedule(schedules)
    }
}

@Dao
interface StopDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStop(stop: Stop)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMultipleStops(stops: List<Stop>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(schedule: Schedule)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMultipleSchedule(schedules: List<Schedule>)

    @Query("SELECT (SELECT COUNT(*) FROM stops) == 0")
    fun isStopsEmpty(): Boolean

    @Query("SELECT (SELECT COUNT(*) FROM schedules WHERE clusterId = :clusterId AND date = :date AND lineId = :lineId) == 0")
    fun isSchedulesEmpty(clusterId:String, date:String, lineId:String): Boolean
}