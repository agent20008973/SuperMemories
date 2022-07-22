package com.lsm.supermemories.ui.memories.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.lsm.supermemories.R
import com.lsm.supermemories.databinding.FragmentTitleListBinding
import com.lsm.supermemories.ui.base.BaseFragment
import com.lsm.supermemories.ui.memories.list.adapters.TitleAdapter
import com.lsm.supermemories.viewmodels.TitleListViewModelFactory
import com.lsm.supermemories.viewmodels.TitleViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TitleFragment : BaseFragment(){

    private val mArgs: TitleFragmentArgs by navArgs()
    private var mBinding: FragmentTitleListBinding? = null
    lateinit var adapter: TitleAdapter

    @Inject
    lateinit var titleListViewModelFactory: TitleListViewModelFactory

    val titleViewModel: TitleViewModel by viewModels {
        TitleViewModel.provideFactory(titleListViewModelFactory, mArgs.Date)
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTitleListBinding>(
                inflater, R.layout.fragment_title_list, container, false

        )

        this.mBinding= binding
        adapter = TitleAdapter(titleViewModel)
        subscribeUi(adapter,binding)

        return binding.root
    }

    private fun subscribeUi(newAdapter: TitleAdapter, binding: FragmentTitleListBinding) {
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            recyclerView.adapter = newAdapter
            viewModel =titleViewModel
        }

        titleViewModel.titles.observe(viewLifecycleOwner) {
            newAdapter.submitList(it)
            newAdapter.notifyDataSetChanged()
        }

        observeModelNavigation(titleViewModel)
    }


}