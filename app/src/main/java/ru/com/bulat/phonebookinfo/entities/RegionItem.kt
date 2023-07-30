package ru.com.bulat.phonebookinfo.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "region_item")
data class RegionItem(
    @PrimaryKey()
    val id: Int,
    @ColumnInfo(name = "region")
    val region:String,
)
