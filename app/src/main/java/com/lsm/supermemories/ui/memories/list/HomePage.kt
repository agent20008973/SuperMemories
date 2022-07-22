package com.lsm.supermemories.ui.memories.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lsm.supermemories.R
import com.lsm.supermemories.databinding.FragmentHomePageBinding
import com.lsm.supermemories.ui.base.BaseFragment
import com.lsm.supermemories.utils.ImageUtils
import com.lsm.supermemories.viewmodels.SentenceViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomePage: BaseFragment() {

    private var mBinding: FragmentHomePageBinding? = null
    private val sentenceViewModel: SentenceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = DataBindingUtil.inflate<FragmentHomePageBinding>(
            inflater, R.layout.fragment_home_page, container, false
        )






        this.mBinding = fragmentBinding

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sentenceViewModel
            homePageFragment = this@HomePage
            imageUtils = ImageUtils
        }
          observeModelNavigation(sentenceViewModel)


    }

    fun navigate_to_add_fragment() {
        findNavController().navigate(R.id.action_homePage_to_downloadMemory)

    }

    fun navigate_to_date_fragment() {
        findNavController().navigate(R.id.action_homePage_to_dateFragment)
    }


}