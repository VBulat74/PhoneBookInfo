package ru.com.bulat.phonebookinfo.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.com.bulat.phonebookinfo.MainApp
import ru.com.bulat.phonebookinfo.databinding.FragmentSettingsBinding
import ru.com.bulat.phonebookinfo.utilits.Resource
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

        initFields()
    }

    private fun initFields() {
        mBinding.btnLoadRangeNumber.setOnClickListener {
            settingsViewModel.InsertRangeNumbers(requireContext())
        }

        settingsViewModel.numberRangeLiveData.observe(viewLifecycleOwner) {resource ->
            when (resource) {
                is Resource.Success -> {
                    mBinding.btnLoadRangeNumber.isClickable = resource.data != true
                    mBinding.btnLoadRangeNumber.isActivated = resource.data != true
                    mBinding.btnLoadRangeNumber.setText("range loaded")
                }
                is Resource.Loading -> {
                    mBinding.btnLoadRangeNumber.isClickable = false
                    mBinding.btnLoadRangeNumber.isActivated = false
                    mBinding.btnLoadRangeNumber.setText("loding range")
                }
                is Resource.Error -> {
                    mBinding.btnLoadRangeNumber.isClickable = true
                    mBinding.btnLoadRangeNumber.isActivated = true
                    mBinding.btnLoadRangeNumber.setText("Error loding range")
                }
            }
        }
    }


}