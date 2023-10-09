package fr.zbug.horairestag.data

import android.os.StrictMode
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import java.net.URL


class LinesRepository(private val lineDao: LineDao) {
    fun getLinesByType(type: String): Flow<List<Line>> {

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        if(lineDao.isEmpty()) {
            val repoListJsonStr = URL("https://data.mobilites-m.fr/api/routers/default/index/routes").readText()
            val jsonObj = JSONArray(repoListJsonStr)

            var lines = ArrayList<Line>()

            for (i in 0 until jsonObj.length()) {
                val jsonLine = jsonObj.getJSONObject(i)
                val jsonType = jsonLine.getString("type")
                if(listOf("TRAM", "FLEXO", "PROXIMO", "CHRONO").contains(jsonType)) {
//                    Log.d("LinesRepository", "Insertion d une ligne $json_type")
                    val line = Line(
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

        return lineDao.getLineByType(type.uppercase())
    }

    suspend fun deleteLine(line: Line) = lineDao.delete(line)

    suspend fun updateLine(line: Line) = lineDao.update(line)
}

@Dao
interface LineDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMultiple(items: List<Line>)

    @Update
    suspend fun update(item: Line)

    @Delete
    suspend fun delete(item: Line)

    @Query("SELECT * from lines WHERE type = :type")
    fun getLineByType(type: String): Flow<List<Line>>

    @Query("SELECT * from lines ORDER BY shortName ASC")
    fun getAllLines(): Flow<List<Line>>

    @Query("SELECT (SELECT COUNT(*) FROM lines) == 0")
    fun isEmpty(): Boolean
}