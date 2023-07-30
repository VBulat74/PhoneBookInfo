package ru.com.bulat.phonebookinfo.ui.fragments.settings

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.com.bulat.phonebookinfo.db.MainDataBase
import ru.com.bulat.phonebookinfo.entities.NumberRangeItem
import ru.com.bulat.phonebookinfo.utilits.Resource
import java.io.BufferedReader
import java.io.InputStreamReader

class SettingsViewModel (database: MainDataBase) : ViewModel() {

    private val dao = database.getDao()

    val numberRangeLiveData : MutableLiveData<Resource<Boolean>> = MutableLiveData()

    fun InsertRangeNumbers(context: Context) = viewModelScope.launch  {
        numberRangeLiveData.postValue(Resource.Loading())

        dao.clearNumberRange()

        val mInput = InputStreamReader(context.assets.open("number_range.csv"))
        val reader = BufferedReader(mInput)

        var line : String

        while (withContext(Dispatchers.IO) {
                reader.readLine()
            }.also { line = it } != null) {
            val row : List<String> = line.split(";")

            val number_range = NumberRangeItem (
                id = null,
                codeDef = if (row[0].length == 4) {
                    row[0].substring(1).toInt()
                } else {
                    row[0].toInt()
                       },
                number_begin = row[1].replace("\"","").toLong(),
                number_end = row[2].replace("\"","").toLong(),
                capacity = row[3].replace("\"","").toLong(),
                provider = row[4],
                region = row[5].replace("\"","").toInt(),
                mnc = row[6].replace("\"","").toInt(),
                route = row[7]
            )
            Log.d("AAA", "Add $number_range")
            dao.insertNumberRange(number_range)
        }

        numberRangeLiveData.postValue(Resource.Success(true))
    }

    class SettingsViewModelFactory(val database: MainDataBase): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            //return super.create(modelClass)
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return SettingsViewModel(database) as T
            }
            throw IllegalArgumentException ("Unknown ViewModelClass")
        }
    }
}