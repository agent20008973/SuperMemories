package com.lsm.supermemories.ui.base
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lsm.supermemories.viewmodels.BaseViewModel

open class BaseFragment : Fragment() {

    internal fun observeModelNavigation(model : BaseViewModel) {
        model.navigateToFragment.observe(this.viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                findNavController().navigate(it.navigateAction, it.getParametersAsBundle())
            }
        })
    }
}