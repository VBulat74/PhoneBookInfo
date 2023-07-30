package ru.com.bulat.phonebookinfo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.com.bulat.phonebookinfo.entities.ContactItem
import ru.com.bulat.phonebookinfo.entities.NumberRangeItem

@Dao
interface Dao {
    @Insert
    suspend fun insertContact(contact:ContactItem)

    @Insert
    suspend fun insertNumberRange(number_range:NumberRangeItem)

    @Update
    suspend fun updateContact(contact:ContactItem)

    @Query("SELECT * FROM contact_item")
    suspend fun getAllContactItemList (): List<ContactItem>

    @Query ("SELECT * FROM contact_item WHERE name LIKE :substring")
    suspend fun getContactItemList (substring: String): List<ContactItem>

    @Query("DELETE FROM contact_item")
    suspend fun clearContacts()

    @Query("DELETE FROM number_range_item")
    suspend fun clearNumberRange()

}