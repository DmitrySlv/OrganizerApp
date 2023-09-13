package com.dscreate_app.organizerapp.activities

import android.app.Application
import com.dscreate_app.organizerapp.data.database.OrganizerDb

class MainApp: Application() {
    val database by lazy { OrganizerDb.getInstance(this) }
}