package ru.com.bulat.phonebookinfo.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_item")
data class ContactItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "row_id")
    val rowID:Long,

    @ColumnInfo(name = "lookup_key")
    val lookupKey:String,

    @ColumnInfo(name = "name")
    val name:String,

    @ColumnInfo(name = "phone")
    val phone:String,

    @ColumnInfo(name = "phone_normalized")
    val phoneNormalized:String,

    @ColumnInfo(name = "photo_thumbnail_uri")
    val photoThumbnailURI: String?,

    @ColumnInfo(name = "photo_uri")
    val photoUri: String?,
)
