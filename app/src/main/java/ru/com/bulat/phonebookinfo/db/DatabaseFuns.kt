package ru.com.bulat.phonebookinfo.db

import ru.com.bulat.phonebookinfo.entities.ContactItem

inline fun initContactDataBase(crossinline function: () -> Unit) {
    function()
}

fun updateContactsDataBase(contactsList: ArrayList<ContactItem>, mainViewModel: MainViewModel) {
    mainViewModel.clearContact()
    contactsList.forEach {contactItem ->
        mainViewModel.insertContactItem(contactItem)
    }
}