package ru.com.bulat.phonebookinfo.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.com.bulat.phonebookinfo.MainApp
import ru.com.bulat.phonebookinfo.databinding.FragmentSettingsBinding
import java.io.InputStreamReader

class SettingsFragment : Fragment() {

    private val settingsViewModel : SettingsViewModel by activityViewModels {
        SettingsViewModel.SettingsViewModelFactory((context?.applicationContext as MainApp).database)
    }

    private lateinit var mBinding : FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.btnLoadRangeNumber.setOnClickListener {
            settingsViewModel.InsertRangeNumbers(requireContext())
        }
    }

    private fun initFields() {

    }


}