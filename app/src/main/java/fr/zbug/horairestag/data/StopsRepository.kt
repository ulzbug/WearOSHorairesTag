package fr.zbug.horairestag.data

import android.os.StrictMode
import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date


class StopsRepository(private val stopDao: StopDao, private val clusterDao: ClusterDao) {
    private suspend fun createStops() {
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

    suspend fun getSchedules(lineId:String, clusterId:String, date:String, direction:Int) : ArrayList<Schedule> {
        if(stopDao.isSchedulesEmpty(lineId, clusterId, date)) {
            this.createStops()

            val schedules = ArrayList<Schedule>()

            Log.d("StopsRepository:getSchedules", "https://data.mobilites-m.fr/api/routers/default/index/clusters/$clusterId/stoptimes/$date?route=$lineId");
            val repoListJsonStr = URL("https://data.mobilites-m.fr/api/routers/default/index/clusters/$clusterId/stoptimes/$date?route=$lineId").readText()
            try {
                val patterns = JSONArray(repoListJsonStr)
                for (i in 0 until patterns.length()) {
                    val pattern = patterns.getJSONObject(i).getJSONObject("pattern")
                    val times = patterns.getJSONObject(i).getJSONArray("times")

                    for (j in 0 until times.length()) {
                        val time = times.getJSONObject(j)

                        schedules.add(
                            Schedule(
                                lineId = lineId,
                                clusterId = clusterId,
                                direction = pattern.getInt("dir"),
                                stopEndId = pattern.getString("lastStop"),
                                date = date,
                                hour = time.getInt("scheduledDeparture")
                            )
                        )

                        if (schedules.size > 1000) {
                            stopDao.insertMultipleSchedule(schedules)
                            schedules.clear()
                        }
                    }
                }
                stopDao.insertMultipleSchedule(schedules)
            } catch (e: JSONException) {
                val error = e.message
                Log.d("StopsRepository:getSchedules", "Erreur parsing Json : $error / $repoListJsonStr")
            }
        }

        val currentDateTimeFR = Date().toInstant().atZone(ZoneId.of("Europe/Paris"))
        val hours = currentDateTimeFR.format(DateTimeFormatter.ofPattern("H")).toInt()
        val minutes = currentDateTimeFR.format(DateTimeFormatter.ofPattern("m")).toInt()
        val secondes = currentDateTimeFR.format(DateTimeFormatter.ofPattern("s")).toInt()
        val currentTime = hours * 3600 + minutes * 60 + secondes

        return ArrayList(stopDao.getNextSchedules(lineId, clusterId, date, currentTime, direction))
    }
}

@Dao
interface StopDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStop(stop: Stop)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultipleStops(stops: List<Stop>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: Schedule)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultipleSchedule(schedules: List<Schedule>)

    @Query("SELECT (SELECT COUNT(*) FROM stops) == 0")
    suspend fun isStopsEmpty(): Boolean

    @Query("SELECT (SELECT COUNT(*) FROM schedules WHERE clusterId = :clusterId AND date = :date AND lineId = :lineId) == 0")
    suspend fun isSchedulesEmpty(lineId:String, clusterId:String, date:String): Boolean

    @Query("SELECT * FROM schedules WHERE clusterId = :clusterId AND date = :date AND lineId = :lineId AND direction = :direction AND hour > :hour ORDER BY hour ASC LIMIT 0,3")
    suspend fun getNextSchedules(lineId:String, clusterId:String, date:String, hour:Int, direction:Int): List<Schedule>
}