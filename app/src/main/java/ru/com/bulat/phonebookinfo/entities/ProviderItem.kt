package ru.com.bulat.phonebookinfo.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "provider_item")
data class ProviderItem(
    @PrimaryKey()
    val id: Int,
    @ColumnInfo(name = "provider")
    val provider:String,
)
