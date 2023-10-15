package fr.zbug.horairestag.data

import android.os.StrictMode
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

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
}