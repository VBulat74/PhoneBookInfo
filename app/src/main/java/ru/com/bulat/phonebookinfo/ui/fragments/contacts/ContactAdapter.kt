package ru.com.bulat.phonebookinfo.ui.fragments.contacts

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
import ru.com.bulat.phonebookinfo.utilits.loadContactPhotoThumbnail
import java.lang.Long.getLong


class ContactAdapter(private val listener: Listener) :
    ListAdapter<ContactItem, ContactAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val mBinding = ContactsListItemBinding.bind(view)

        fun setData(contact: ContactItem, listener: Listener) = with(mBinding) {
            tviewContactName.text = contact.name
            tviewContactPhone.text = contact.phone
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
                    qcbContactPhoto.setImageBitmap(thumbnailBitmap)
                }
            }


            itemView.setOnClickListener {

            }

            qcbContactPhoto.setOnClickListener {
                listener.onClickItem(contact)
            }

            imbuttonCallContact.setOnClickListener {

            }
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