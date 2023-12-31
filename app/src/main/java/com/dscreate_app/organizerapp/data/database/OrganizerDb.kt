package com.dscreate_app.organizerapp.data.database

import android.app.Application
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dscreate_app.organizerapp.data.entities.LibraryItemEntity
import com.dscreate_app.organizerapp.data.entities.NoteItemEntity
import com.dscreate_app.organizerapp.data.entities.ShoppingListItemEntity
import com.dscreate_app.organizerapp.data.entities.ShoppingListNameEntity

@Database(entities = [LibraryItemEntity::class, NoteItemEntity::class,
    ShoppingListItemEntity::class, ShoppingListNameEntity::class], version = 2,
    exportSchema = true, autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class OrganizerDb: RoomDatabase() {

    companion object {
        @Volatile
        private var instance: OrganizerDb? = null
        private val LOCK = Any()
        private const val DB_NAME = "main.db"

        fun getInstance(application: Application): OrganizerDb {
            instance?.let { return it }
            synchronized(LOCK) {
                instance?.let { return it }
                val db = Room.databaseBuilder(
                    application,
                    OrganizerDb::class.java,
                    DB_NAME
                ).build()
                instance = db
                return db
            }
        }
    }

    abstract fun getDao(): Dao
}