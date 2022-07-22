package com.lsm.supermemories.ui.memories.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.lsm.supermemories.R
import com.lsm.supermemories.databinding.FragmentDateListBinding
import com.lsm.supermemories.ui.base.BaseFragment
import com.lsm.supermemories.ui.memories.list.adapters.DateAdapter
import com.lsm.supermemories.viewmodels.DateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DateFragment : BaseFragment(){


    private var mBinding: FragmentDateListBinding? = null
    lateinit var adapter: DateAdapter
    val dateViewModel: DateViewModel by viewModels()


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentDateListBinding>(
                inflater, R.layout.fragment_date_list, container, false

        )

        this.mBinding= binding
        adapter = DateAdapter(dateViewModel)
        subscribeUi(adapter)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding?.apply {
            lifecycleOwner = viewLifecycleOwner
            recyclerView.adapter = adapter
            viewModel = dateViewModel
        }
        observeModelNavigation(dateViewModel)
    }

    private fun subscribeUi(newAdapter: DateAdapter) {
        dateViewModel.dates.observe(viewLifecycleOwner) { result ->
            newAdapter.submitList(result)
            newAdapter.notifyDataSetChanged()
        }
        observeModelNavigation(dateViewModel)
    }



}





