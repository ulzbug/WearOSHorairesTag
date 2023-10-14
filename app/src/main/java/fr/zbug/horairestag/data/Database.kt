package fr.zbug.horairestag.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Line::class, Cluster::class, LineCluster::class, Schedule::class, Stop::class], version = 1, exportSchema = false)
abstract class HorairesTagDatabase : RoomDatabase() {

    abstract fun lineDao(): LineDao
    abstract fun clusterDao(): ClusterDao
    abstract fun stopDao(): StopDao

    companion object {
        @Volatile
        private var Instance: HorairesTagDatabase? = null

        fun getDatabase(context: Context): HorairesTagDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, HorairesTagDatabase::class.java, "horaires_tag_database")
                    .allowMainThreadQueries() // TODO : remove pour gérer en asynchrone
                    .build().also { Instance = it }
            }
        }
    }
}