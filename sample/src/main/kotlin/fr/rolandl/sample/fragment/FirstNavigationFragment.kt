package fr.rolandl.sample.fragment

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import fr.rolandl.sample.BR
import fr.rolandl.sample.R
import fr.rolandl.sample.databinding.FragmentFirstNavigationBinding
import fr.rolandl.sample.viewmodel.FirstNavigationFragmentViewModel
import fr.rolandl.rijsel.content.RijselSharedFlowListener
import fr.rolandl.rijsel.content.RijselSharedFlowListenerProvider
import fr.rolandl.rijsel.fragment.app.RijselFragmentConfigurable

/**
 * @author Ludovic Roland
 * @since 2022/03/22
 */
class FirstNavigationFragment :
    SampleFragment<FragmentFirstNavigationBinding, FirstNavigationFragmentViewModel>(),
    RijselSharedFlowListenerProvider
{

  override fun getRetryView(): View? =
      null

  override fun layoutId(): Int =
      R.layout.fragment_first_navigation

  override fun getViewModelClass(): Class<FirstNavigationFragmentViewModel> =
      FirstNavigationFragmentViewModel::class.java

  override fun viewModelBehavior(): RijselFragmentConfigurable.ViewModelBehavior =
    RijselFragmentConfigurable.ViewModelBehavior.Reload

  override fun getBindingVariable(): Int =
      BR.model

  override fun getRijselSharedFlowListener(): RijselSharedFlowListener
  {
    return object : RijselSharedFlowListener
    {
      override fun getIntentFilter(): IntentFilter =
          IntentFilter("ACTION").apply {
            addCategory(getIdentityHashCode())
          }

      override fun onCollect(intent: Intent)
      {
        if (intent.action == "ACTION")
        {
          //Timber.d("${this@FirstNavigationFragment.javaClass} receives action")
          viewModel?.incrementCounter()
        }
      }
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?)
  {
    super.onViewCreated(view, savedInstanceState)
    viewModel?.fragmentHostIdentityHashcode = getIdentityHashCode()
  }

  private fun getIdentityHashCode() =
      System.identityHashCode(this).toString()
}