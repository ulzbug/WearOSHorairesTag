package fr.zbug.horairestag

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import fr.zbug.horairestag.data.HorairesTagDatabase
import fr.zbug.horairestag.data.LinesRepository

class HorairesTagApplication : Application() {
    val database: HorairesTagDatabase by lazy { HorairesTagDatabase.getDatabase(this) }
}