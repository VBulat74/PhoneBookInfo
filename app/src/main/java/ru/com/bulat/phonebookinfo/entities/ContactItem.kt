package ru.com.bulat.phonebookinfo.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_item")
data class ContactItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "name")
    val name:String,

    @ColumnInfo(name = "phone")
    val phone:String,

    @ColumnInfo(name = "photo")
    val photo:String,
)
