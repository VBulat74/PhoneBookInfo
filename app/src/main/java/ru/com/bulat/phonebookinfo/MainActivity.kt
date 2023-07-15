package ru.com.bulat.phonebookinfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.com.bulat.phonebookinfo.databinding.ActivityMainBinding
import ru.com.bulat.phonebookinfo.ui.fragments.contacts.ContactsFragment
import ru.com.bulat.phonebookinfo.utilits.APP_ACTIVITY
import ru.com.bulat.phonebookinfo.utilits.replaceFragment

class MainActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainBinding
    private var currentMenuItemId = R.id.contacts_book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        APP_ACTIVITY = this

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        setBottomNavListener()
        initFields()
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
}