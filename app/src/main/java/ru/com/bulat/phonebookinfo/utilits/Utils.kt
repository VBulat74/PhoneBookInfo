package ru.com.bulat.phonebookinfo.utilits

import androidx.fragment.app.Fragment
import ru.com.bulat.phonebookinfo.R

fun replaceFragment(fragment: Fragment, addStack: Boolean = true) {

    if (addStack) {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.data_container,
                fragment
            ).commit()
    } else {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .replace(
                R.id.data_container,
                fragment
            ).commit()
    }
}