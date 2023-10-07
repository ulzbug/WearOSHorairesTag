package fr.zbug.horairestag.data

import android.os.StrictMode
import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.toList
import org.json.JSONArray
import java.net.URL


class LinesRepository(private val lineDao: LineDao) {
    fun getLinesByType(type: String): Flow<List<Line>> {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        if(lineDao.isEmpty()) {
            Log.d("Pouet", "Database empty")
            val repoListJsonStr = URL("https://data.mobilites-m.fr/api/routers/default/index/routes").readText()
            val jsonObj = JSONArray(repoListJsonStr)
            for (i in 0 until jsonObj!!.length()) {
                val jsonLine = jsonObj.getJSONObject(i)
                val json_type = jsonLine.getString("type")
                if(listOf("TRAM", "FLEXO", "PROXIMO", "CHRONO").contains(json_type)) {
                    Log.d("Pouet", "Insertion d une ligne $json_type")
                    val line = Line(
                        gtfsId = jsonLine.getString("gtfsId"),
                        shortName = jsonLine.getString("shortName"),
                        longName = jsonLine.getString("longName"),
                        color = jsonLine.getString("color"),
                        textColor = jsonLine.getString("textColor"),
                        mode = jsonLine.getString("mode"),
                        type = json_type
                    )
                    lineDao.insert(line)
                }
            }
            Log.d("Pouet", "Insertion terminée")
        } else {
            Log.d("Pouet", "Database not empty")
        }

        return lineDao.getLineByType(type.uppercase())
    }

    fun insertLine(line: Line) = lineDao.insert(line)

    suspend fun deleteLine(line: Line) = lineDao.delete(line)

    suspend fun updateLine(line: Line) = lineDao.update(line)
}

@Dao
interface LineDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: Line)

    @Update
    suspend fun update(item: Line)

    @Delete
    suspend fun delete(item: Line)

    @Query("SELECT * from lines WHERE type = :type")
    fun getLineByType(type: String): Flow<List<Line>>

    @Query("SELECT * from lines ORDER BY shortName ASC")
    fun getAllLinesCode(): Flow<List<Line>>

    @Query("SELECT * from lines ORDER BY shortName ASC")
    fun getAllLines(): Flow<List<Line>>

    @Query("SELECT (SELECT COUNT(*) FROM lines) == 0")
    fun isEmpty(): Boolean
}