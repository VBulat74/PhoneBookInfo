package ru.com.bulat.phonebookinfo.ui.fragments.contacts

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.com.bulat.phonebookinfo.R
import ru.com.bulat.phonebookinfo.databinding.ContactsListItemBinding
import ru.com.bulat.phonebookinfo.entities.ContactItem
import ru.com.bulat.phonebookinfo.utilits.APP_ACTIVITY
import ru.com.bulat.phonebookinfo.utilits.loadContactPhotoThumbnail


class ContactAdapter(private val listener: Listener) :
    ListAdapter<ContactItem, ContactAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val mBinding = ContactsListItemBinding.bind(view)

        fun setData(contact: ContactItem, listener: Listener) = with(mBinding) {
            tviewContactName.text = contact.name
            tviewContactPhone.text = APP_ACTIVITY.getString(
                R.string.contacts_phone_number,
                contact.phoneType,
                contact.phone
            )
            tviewContactInfo.text = "Id: ${contact.rowID}"

            /*
            * Generates a contact URI for the QuickContactBadge
            */
            ContactsContract.Contacts.getLookupUri(
                contact.rowID,
                contact.lookupKey,
            ).also { contactUri ->
                Log.d("AAA", "contactUri: $contactUri")
                qcbContactPhoto.assignContactUri(contactUri)
            }

            val photoURI = contact.photoThumbnailURI
            if (photoURI != null) {
                loadContactPhotoThumbnail(contact.photoThumbnailURI)?.also { thumbnailBitmap ->
                    qcbContactPhoto.setImageBitmap(getCroppedBitmap(thumbnailBitmap))
                }
            }


            itemView.setOnClickListener {

            }

//            qcbContactPhoto.setOnClickListener {
//                listener.onClickItem(contact)
//            }

            imbuttonCallContact.setOnClickListener {

            }
        }

        fun getCroppedBitmap(bitmap: Bitmap): Bitmap? {
            val output = Bitmap.createBitmap(
                bitmap.width,
                bitmap.height, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(output)
            val color = -0xbdbdbe
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width, bitmap.height)
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = color
            // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            canvas.drawCircle(
                (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
                (
                        bitmap.width / 2).toFloat(), paint
            )
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)
            // Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
            // return _bmp;
            return output
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.contacts_list_item, parent, false)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }


    class ItemComparator : DiffUtil.ItemCallback<ContactItem>() {
        override fun areItemsTheSame(oldItem: ContactItem, newItem: ContactItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ContactItem, newItem: ContactItem): Boolean {
            return oldItem == newItem
        }

    }

    interface Listener {
        fun onClickItem(contact: ContactItem)
    }


}