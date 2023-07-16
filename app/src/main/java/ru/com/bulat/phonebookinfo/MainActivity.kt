package ru.com.bulat.phonebookinfo

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.com.bulat.phonebookinfo.databinding.ActivityMainBinding
import ru.com.bulat.phonebookinfo.db.MainViewModel
import ru.com.bulat.phonebookinfo.db.initContactDataBase
import ru.com.bulat.phonebookinfo.ui.fragments.contacts.ContactsFragment
import ru.com.bulat.phonebookinfo.utilits.APP_ACTIVITY
import ru.com.bulat.phonebookinfo.utilits.READ_CONTACTS
import ru.com.bulat.phonebookinfo.utilits.initContacts
import ru.com.bulat.phonebookinfo.utilits.replaceFragment

class MainActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainBinding
    private var currentMenuItemId = R.id.contacts_book

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        APP_ACTIVITY = this

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        initContactDataBase {
            CoroutineScope(Dispatchers.IO).launch {
                initContacts(mainViewModel)
            }
            setBottomNavListener()
            initFields()
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.mainBottomNavigation.setSelectedItemId(currentMenuItemId)
    }

    private fun setBottomNavListener() {
        mBinding.mainBottomNavigation.setOnItemSelectedListener {item ->
            when (item.itemId) {
                R.id.contacts_book -> {
                    replaceFragment(ContactsFragment(),false)
                    currentMenuItemId = R.id.contacts_book
                }
            }
            true
        }
    }

    private fun initFields() {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(ContextCompat.checkSelfPermission(APP_ACTIVITY, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            initContacts(mainViewModel)
        }
    }
}