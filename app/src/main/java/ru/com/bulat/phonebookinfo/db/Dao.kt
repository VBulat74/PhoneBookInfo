package ru.com.bulat.phonebookinfo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.com.bulat.phonebookinfo.entities.ContactItem

@Dao
interface Dao {
    @Insert
    suspend fun insertContact(contact:ContactItem)

    @Update
    suspend fun updateContact(contact:ContactItem)

    @Query("SELECT * FROM contact_item")
    fun getAllNotes (): Flow<List<ContactItem>>

    @Query ("SELECT * FROM contact_item WHERE name LIKE :substring")
    suspend fun getContactItemList (substring: String): List<ContactItem>

}