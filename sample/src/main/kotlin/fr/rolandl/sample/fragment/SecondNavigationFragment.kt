package fr.rolandl.sample.fragment

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import fr.rolandl.sample.BR
import fr.rolandl.sample.R
import fr.rolandl.sample.databinding.FragmentSecondNavigationBinding
import fr.rolandl.sample.viewmodel.SecondNavigationFragmentViewModel
import fr.rolandl.rijsel.content.RijselSharedFlowListener
import fr.rolandl.rijsel.content.RijselSharedFlowListenerProvider
import fr.rolandl.rijsel.fragment.app.RijselFragmentConfigurable
import com.xwray.groupie.GroupieAdapter
import timber.log.Timber

/**
* @author Ludovic Roland
* @since 2022/03/22
*/
class SecondNavigationFragment :
    SampleFragment<FragmentSecondNavigationBinding, SecondNavigationFragmentViewModel>(),
    RijselSharedFlowListenerProvider
{
  private val adapter by lazy { GroupieAdapter() }

  override fun getRetryView(): View? =
      null

  override fun layoutId(): Int =
      R.layout.fragment_second_navigation

  override fun getViewModelClass(): Class<SecondNavigationFragmentViewModel> =
      SecondNavigationFragmentViewModel::class.java

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
          Timber.d("${this@SecondNavigationFragment.javaClass} receives action")
          viewModel?.incrementCounter()
        }
      }
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?)
  {
    super.onViewCreated(view, savedInstanceState)
    viewModel?.fragmentHostIdentityHashcode = getIdentityHashCode()

    viewDatabinding?.list?.adapter = adapter
    viewDatabinding?.list?.setHasFixedSize(true)

    viewModel?.items?.observe(viewLifecycleOwner) { items ->
      adapter.update(items)
    }

    Toast.makeText(requireContext(), getIdentityHashCode(), Toast.LENGTH_LONG).show()
  }

  override fun onPause() {
    Timber.d("onPause")
    super.onPause()
  }

  override fun onStop() {
    Timber.d("onStop")
    super.onStop()
  }

  override fun onDestroy() {
    Timber.d("onDestroy")
    super.onDestroy()
  }

  override fun onDestroyView() {
    Timber.d("onDestroyView")
    super.onDestroyView()
  }

  private fun getIdentityHashCode() =
      System.identityHashCode(this).toString()
}