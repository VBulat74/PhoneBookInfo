package ru.com.bulat.phonebookinfo.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "number_range_item")
data class NumberRangeItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "code_def")
    val codeDef:Int,
    @ColumnInfo(name = "number_begin")
    val number_begin:Long,
    @ColumnInfo(name = "number_end")
    val number_end:Long,
    @ColumnInfo(name = "capacity")
    val capacity : Long,
    @ColumnInfo(name = "provider")
    val provider : String,
    @ColumnInfo(name = "region")
    val region : Int,
    @ColumnInfo(name = "mnc")
    val mnc : Int,
    @ColumnInfo(name = "route")
    val route : String,


)
