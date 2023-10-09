package fr.zbug.horairestag

import android.app.Application
import fr.zbug.horairestag.data.HorairesTagDatabase

class HorairesTagApplication : Application() {
    val database: HorairesTagDatabase by lazy { HorairesTagDatabase.getDatabase(this) }
}