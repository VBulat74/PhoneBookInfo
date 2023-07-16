package ru.com.bulat.phonebookinfo.db

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.com.bulat.phonebookinfo.entities.ContactItem

class MainViewModel (database: MainDataBase) : ViewModel() {
    private val dao = database.getDao()

    val contactItems = MutableLiveData<List<ContactItem>>()

    fun getContactItemList(substring: String) = viewModelScope.launch{
        contactItems.postValue(dao.getContactItemList(substring))
    }


    class MainViewModelFactory(val database: MainDataBase): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            //return super.create(modelClass)
            if (modelClass.isAssignableFrom(MainViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T
            }
            throw IllegalArgumentException ("Unknown ViewModelClass")
        }
    }
}