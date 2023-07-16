package ru.com.bulat.phonebookinfo.ui.fragments.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.com.bulat.phonebookinfo.MainApp
import ru.com.bulat.phonebookinfo.databinding.FragmentContactsBinding
import ru.com.bulat.phonebookinfo.db.MainViewModel
import ru.com.bulat.phonebookinfo.entities.ContactItem

class ContactsFragment : Fragment(), ContactAdapter.Listener {

    private lateinit var mBinding : FragmentContactsBinding
    private lateinit var adapter : ContactAdapter

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentContactsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        listContactItensObserver()
    }

    private fun listContactItensObserver() {
        mainViewModel.contactItems.observe(viewLifecycleOwner) {listContactItems ->
            adapter.submitList(listContactItems)
        }
    }

    private fun initRcView() = with(mBinding) {
        rcViewContacts.layoutManager = LinearLayoutManager (activity)
        adapter = ContactAdapter(this@ContactsFragment)
    }

    override fun onClickItem(contact: ContactItem) {

    }
}