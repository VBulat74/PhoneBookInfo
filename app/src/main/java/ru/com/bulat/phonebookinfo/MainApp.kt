package ru.com.bulat.phonebookinfo

import android.app.Application
import ru.com.bulat.phonebookinfo.db.MainDataBase

class MainApp : Application () {
    val database by lazy { MainDataBase.getDataBase(this) }

}