package fr.zbug.horairestag.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


class LinesRepository(private val lineDao: LineDao) {
    suspend fun getLinesByType(type: String): List<Line> {

        if(lineDao.isEmpty()) {
            TagRepository().getLines(lineDao)
        }

        return lineDao.getLineByType(type.uppercase())
    }

    suspend fun getLine(id: String): Line = lineDao.getLine(id)
}

@Dao
interface LineDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMultiple(items: List<Line>)

    @Query("SELECT * from lines WHERE id = :id")
    suspend fun getLine(id: String): Line

    @Query("SELECT * from lines WHERE type = :type")
    suspend fun getLineByType(type: String): List<Line>

    @Query("SELECT (SELECT COUNT(*) FROM lines) == 0")
    suspend fun isEmpty(): Boolean
}