package ru.com.bulat.phonebookinfo.utilits

import android.annotation.SuppressLint
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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

        val cursorContact = APP_ACTIVITY.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null,
            null,
        )

        if ((cursorContact?.count ?: 0) > 0) {
            while (cursorContact != null && cursorContact.moveToNext()) {

                val rowID =
                    cursorContact.getLong(cursorContact.getColumnIndex(ContactsContract.Contacts._ID))
                val lookupKey =
                    cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                val name =
                    cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val photoThumbnailURI =
                    cursorContact.getStringOrNull(cursorContact.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
                val photoURI =
                    cursorContact.getStringOrNull(cursorContact.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))

                val hasPhone =
                    cursorContact.getInt(cursorContact.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                var phoneNumber = ""
                var phoneNormalizedNumber = ""
                var phoneType = ""
                if (hasPhone > 0) {

                    val cursorPhone = APP_ACTIVITY.contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf<String>(rowID.toString()),
                        null
                    )
                    Log.d("AAA", "rowID: $rowID")
                    if (cursorPhone != null) {
                        while (cursorPhone.moveToNext()) {
                            phoneNumber =
                                cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            phoneNormalizedNumber =
                                cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER))
                            val type =
                                cursorPhone.getInt(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
                            val label =
                                cursorPhone.getInt(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL))
                            phoneType = when (type) {
                                ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> "home"
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> "mobile"
                                ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> "work"
                                ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK -> "fax work"
                                ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME -> "fax home"
                                ContactsContract.CommonDataKinds.Phone.TYPE_PAGER -> "pager"
                                ContactsContract.CommonDataKinds.Phone.TYPE_OTHER -> "other"
                                ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK -> "callback"
                                ContactsContract.CommonDataKinds.Phone.TYPE_CAR -> "car"
                                ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN -> "company main"
                                ContactsContract.CommonDataKinds.Phone.TYPE_ISDN -> "isdn"
                                ContactsContract.CommonDataKinds.Phone.TYPE_MAIN -> "main"
                                ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX -> "other fax"
                                ContactsContract.CommonDataKinds.Phone.TYPE_RADIO -> "radio"
                                ContactsContract.CommonDataKinds.Phone.TYPE_TELEX -> "telex"
                                ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD -> "tty tdd"
                                ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE -> "work mobile"
                                ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER -> "work pager"
                                ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT -> "assistant"
                                ContactsContract.CommonDataKinds.Phone.TYPE_MMS -> "mms"
                                else -> "other"
                            }

                            val newContact = ContactItem(
                                id = null,
                                rowID = rowID,
                                lookupKey = lookupKey,
                                name = name,
                                phone = phoneNumber,
                                phoneNormalized = phoneNormalizedNumber,
                                phoneType = phoneType,
                                photoThumbnailURI = photoThumbnailURI,
                                photoUri = photoURI,
                            )
                            Log.d(
                                "AAA",
                                "label: $label;  contact: ${newContact.toString()}"
                            )

                            contactsList.add(newContact)
                        }
                        cursorPhone.close()
                    }
                }
            }
        }
        cursorContact?.close()
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
        return afd?.fileDescriptor?.let { fileDescriptor ->
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
