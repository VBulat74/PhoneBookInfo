package ru.com.bulat.phonebookinfo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.com.bulat.phonebookinfo.entities.ContactItem
import ru.com.bulat.phonebookinfo.entities.NumberRangeItem
import ru.com.bulat.phonebookinfo.entities.RegionItem

@Database(entities = [
    ContactItem::class,
    NumberRangeItem::class,
    RegionItem::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class MainDataBase : RoomDatabase() {

    abstract fun getDao():Dao

    companion object{
        @Volatile
        private var INSTANCE: MainDataBase? = null

        fun getDataBase(context: Context):MainDataBase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "pone_book_info.db"
                ).build()
                instance
            }
        }
    }
}