package com.lsm.supermemories.ui.memories.list

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.lsm.supermemories.MainApplication
import com.lsm.supermemories.R
import com.lsm.supermemories.databinding.FragmentMemoryInfoBinding
import com.lsm.supermemories.ui.base.BaseFragment
import com.lsm.supermemories.utils.ImageUtils
import com.lsm.supermemories.viewmodels.MemoryInfoViewModel
import com.lsm.supermemories.viewmodels.MemoryInfoViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
@AndroidEntryPoint
class MemoryInfoFragment: BaseFragment(){
private val args: MemoryInfoFragmentArgs by navArgs()
private var mBinding: FragmentMemoryInfoBinding? = null

@Inject
lateinit var memoryInfoViewModelFactory: MemoryInfoViewModelFactory

private val memoryInfoViewModel: MemoryInfoViewModel by viewModels {
 MemoryInfoViewModel.provideFactory(memoryInfoViewModelFactory, args.title)
}

override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
): View? {
 val fragmentBinding = DataBindingUtil.inflate<FragmentMemoryInfoBinding>(
         inflater, R.layout.fragment_memory_info, container, false
 )

 mBinding = fragmentBinding

 return fragmentBinding.root
}

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 super.onViewCreated(view, savedInstanceState)

 mBinding?.apply {
  lifecycleOwner = viewLifecycleOwner
  memoryModel = memoryInfoViewModel
  memoryFragment = this@MemoryInfoFragment
  imageUtils = ImageUtils
 }

 observeModelNavigation(memoryInfoViewModel)

 memoryInfoViewModel.show_memory()
show_image()




}

 fun show_image(){

  val context = MainApplication.applicationContext()
  var sharedPreferences = context.getSharedPreferences("Image", Context.MODE_PRIVATE)
  val json = sharedPreferences.getString("Image", "")
  val c  = BitmapFactory.decodeFile(json)
  mBinding!!.imageView.load(c)

 }


}


