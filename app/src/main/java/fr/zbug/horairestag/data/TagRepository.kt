package fr.zbug.horairestag.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URL

class TagRepository {
     private suspend fun getUrl(url: String) : String {
        Log.d("TagRepository", url);
        var result = "";
        try {
            withContext(Dispatchers.IO) {
                result = URL(url).readText()
            }
        } catch (e : Exception) {
            val error = e.message
            Log.e("TagRepository:getUrl","Impossible de charger l'url : $url. $error")
            throw Exception(e)
        }

        return result
    }
    suspend fun getSchedules(lineId:String, clusterId:String, date:String) : ArrayList<Schedule> {
        val schedules = ArrayList<Schedule>()
        val repoListJsonStr = getUrl("https://data.mobilites-m.fr/api/routers/default/index/clusters/$clusterId/stoptimes/$date?route=$lineId")
        Log.d("TagRepository:getSchedules", repoListJsonStr)
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
                }
            }
        } catch (e: JSONException) {
            val error = e.message
            Log.d("TagRepository:getSchedules", "Erreur parsing Json : $error / $repoListJsonStr")
        }

        return schedules
    }

    suspend fun getLines(lineDao : LineDao) {
        val repoListJsonStr = getUrl("https://data.mobilites-m.fr/api/routers/default/index/routes")
        val jsonObj = JSONArray(repoListJsonStr)

        var lines = ArrayList<Line>()

        for (i in 0 until jsonObj.length()) {
            val jsonLine = jsonObj.getJSONObject(i)
            val jsonType = jsonLine.getString("type")
            if(listOf("TRAM", "FLEXO", "PROXIMO", "CHRONO").contains(jsonType)) {
                val line = Line(
                    id = jsonLine.getString("id"),
                    gtfsId = jsonLine.getString("gtfsId"),
                    shortName = jsonLine.getString("shortName"),
                    longName = jsonLine.getString("longName"),
                    color = jsonLine.getString("color"),
                    textColor = jsonLine.getString("textColor"),
                    mode = jsonLine.getString("mode"),
                    type = jsonType
                )
                lines.add(line)
            }
        }

        lineDao.insertMultiple(lines)
    }

    suspend fun getClusters(clusterDao : ClusterDao, lineId : String) {
        val repoListJsonStr = getUrl("https://data.mobilites-m.fr/api/routers/default/index/routes/$lineId/clusters")

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

    suspend fun getStops(stopDao : StopDao, clusterDao : ClusterDao) {
        val repoListJsonStr =
            URL("https://data.mobilites-m.fr/api/points/json?types=stops%2Cclusters").readText()
        val jsonObj = JSONObject(repoListJsonStr).getJSONArray("features")

        val clusters = ArrayList<Cluster>()
        val stops = ArrayList<Stop>()

        for (i in 0 until jsonObj.length()) {
            val jsonCluster = jsonObj.getJSONObject(i)
            val properties = jsonCluster.getJSONObject("properties")
            val coords = jsonCluster.getJSONObject("geometry").getJSONArray("coordinates")
            if (properties.getString("type") == "clusters") {
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

                if (clusters.size > 1000) {
                    clusterDao.insertMultiple(clusters)
                    clusters.clear()
                }

            } else if (properties.getString("type") == "stops") {
                val stop = Stop(
                    gtfsId = properties.getString("id"),
                    city = properties.getString("city"),
                    name = properties.getString("name"),
                    lat = coords.getDouble(0),
                    lon = coords.getDouble(1),
                    clusterGtfsId = properties.getString("clusterGtfsId")
                )
                stops.add(stop)

                if (stops.size > 1000) {
                    stopDao.insertMultipleStops(stops)
                    stops.clear()
                }
            }

            clusterDao.insertMultiple(clusters)
            stopDao.insertMultipleStops(stops)
        }
    }
}