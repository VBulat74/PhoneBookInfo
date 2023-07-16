package ru.com.bulat.phonebookinfo.utilits

import android.annotation.SuppressLint
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.util.Log
import androidx.core.database.getStringOrNull
import ru.com.bulat.phonebookinfo.db.MainViewModel
import ru.com.bulat.phonebookinfo.db.updateContactsDataBase
import ru.com.bulat.phonebookinfo.entities.ContactItem
import java.io.FileNotFoundException
import java.io.IOException

@SuppressLint("Range")
fun initContacts(mainViewModel: MainViewModel) {
    if (checkPermission(READ_CONTACTS)) {
        val contactsList = ArrayList<ContactItem>()

        val contactCursor = APP_ACTIVITY.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null,
            null,
        )

        if ((contactCursor?.count ?: 0) > 0) {
            while (contactCursor != null && contactCursor.moveToNext()){

                val rowID  = contactCursor.getLong(contactCursor.getColumnIndex(ContactsContract.Contacts._ID))
                val lookupKey = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                val name =  contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val photoThumbnailURI = contactCursor.getStringOrNull(contactCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
                val photoURI = contactCursor.getStringOrNull(contactCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))

                val hasPhone = contactCursor.getInt(contactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                var phoneNumber = ""
                var phoneNormalizedNumber = ""
                if (hasPhone > 0) {
                    phoneNumber = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    phoneNormalizedNumber = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER))
                }

                val newContact  = ContactItem (
                    id = null,
                    rowID = rowID,
                    lookupKey = lookupKey,
                    name = name,
                    phone = phoneNumber,
                    phoneNormalized = phoneNormalizedNumber,
                    photoThumbnailURI = photoThumbnailURI,
                    photoUri = photoURI,
                )
                Log.d("AAA", newContact.toString())
                contactsList.add(newContact)
            }
        }
        contactCursor?.close()
        updateContactsDataBase(contactsList, mainViewModel)
    }
}

/**
 * Load a contact photo thumbnail and return it as a Bitmap,
 * resizing the image to the provided image dimensions as needed.
 * @param photoData photo ID Prior to Honeycomb, the contact's _ID value.
 * For Honeycomb and later, the value of PHOTO_THUMBNAIL_URI.
 * @return A thumbnail Bitmap, sized to the provided width and height.
 * Returns null if the thumbnail is not found.
 */
fun loadContactPhotoThumbnail(photoData: String): Bitmap? {
    // Creates an asset file descriptor for the thumbnail file
    var afd: AssetFileDescriptor? = null
    // try-catch block for file not found
    try {
        // Creates a holder for the URI
        val thumbUri: Uri =
            Uri.parse(photoData)

        /*
         * Retrieves an AssetFileDescriptor object for the thumbnail URI
         * using ContentResolver.openAssetFileDescriptor
         */
        afd = APP_ACTIVITY.contentResolver?.openAssetFileDescriptor(thumbUri, "r")
        /*
         * Gets a file descriptor from the asset file descriptor.
         * This object can be used across processes.
         */
        return afd?.fileDescriptor?.let {fileDescriptor ->
            // Decodes the photo file and returns the result as a Bitmap
            // if the file descriptor is valid
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, null)
        }
    } catch (e: FileNotFoundException) {
        /*
         * Handle file not found errors
         */
        return null
    } finally {
        // In all cases, close the asset file descriptor
        try {
            afd?.close()
        } catch (e: IOException) {
        }
    }
}
