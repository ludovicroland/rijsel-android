package fr.rolandl.sample.fragment

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.rolandl.sample.NavigationActivity
import fr.rolandl.sample.R
import fr.rolandl.sample.SecondActivity
import fr.rolandl.sample.ThirdActivity
import fr.rolandl.sample.databinding.FragmentMainBinding
import fr.rolandl.sample.viewmodel.MainFragmentViewModel
import fr.rolandl.rijsel.content.LocalSharedFlowManager
import fr.rolandl.rijsel.content.RijselSharedFlowListener
import fr.rolandl.rijsel.content.RijselSharedFlowListenerProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author Ludovic Roland
 * @since 2018.11.08
 */
class MainFragment
  : SampleFragment<FragmentMainBinding, MainFragmentViewModel>(),
    View.OnClickListener, RijselSharedFlowListenerProvider
{

  companion object
  {

    const val MY_ACTION = "myAction"

  }

  override fun getRijselSharedFlowListener(): RijselSharedFlowListener
  {
    return object : RijselSharedFlowListener
    {
      override fun getIntentFilter(): IntentFilter =
          IntentFilter(MY_ACTION)

      override fun onCollect(intent: Intent)
      {
        if (intent.action == MY_ACTION)
        {
          Toast.makeText(context, "Click !", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  override fun layoutId(): Int =
      R.layout.fragment_main

  override fun fragmentTitleId(): Int =
      R.string.app_name

  override fun getViewModelClass(): Class<MainFragmentViewModel> =
      MainFragmentViewModel::class.java

  override fun getDispatcher(): CoroutineDispatcher =
      Dispatchers.Default

  override fun onViewCreated(view: View, savedInstanceState: Bundle?)
  {
    super.onViewCreated(view, savedInstanceState)

    viewDatabinding?.openBinding?.setOnClickListener(this)
    viewDatabinding?.openBinding2?.setOnClickListener(this)
    viewDatabinding?.click?.setOnClickListener(this)
    viewDatabinding?.refreshLoading?.setOnClickListener(this)
    viewDatabinding?.refreshNoLoading?.setOnClickListener(this)
    viewDatabinding?.refreshError?.setOnClickListener(this)
    viewDatabinding?.refreshInternetError?.setOnClickListener(this)
    viewDatabinding?.alertDialog?.setOnClickListener(this)
    viewDatabinding?.navigation?.setOnClickListener(this)
  }

  override fun onClick(view: View?)
  {
    when (view)
    {
      viewDatabinding?.openBinding          ->
      {
        val intent = Intent(context, SecondActivity::class.java).apply {
          putExtra(SecondFragment.MY_EXTRA, "hey !")
          putExtra(SecondFragment.ANOTHER_EXTRA, "Another Hey !")
        }

        startActivity(intent)
      }
      viewDatabinding?.openBinding2         ->
      {
        val intent = Intent(context, ThirdActivity::class.java).apply {
          putExtra(ThirdFragment.MY_EXTRA, "go !")
          putExtra(ThirdFragment.ANOTHER_EXTRA, "Another go !")
        }

        startActivity(intent)
      }
      viewDatabinding?.click                ->
      {
        LocalSharedFlowManager.emit(lifecycleScope, Intent(MY_ACTION))
      }
      viewDatabinding?.refreshLoading       ->
      {
        viewModel?.refreshViewModel(true) {
          Toast.makeText(context, "Finish !", Toast.LENGTH_SHORT).show()
        }
      }
      viewDatabinding?.refreshNoLoading     ->
      {
        viewModel?.refreshViewModel(false) {
          Toast.makeText(context, "Finish !", Toast.LENGTH_SHORT).show()
        }
      }
      viewDatabinding?.refreshError         ->
      {
        viewModel?.apply {
          throwError = true
          refreshViewModel(true) {
            Toast.makeText(context, "Finish !", Toast.LENGTH_SHORT).show()
          }
        }
      }
      viewDatabinding?.refreshInternetError ->
      {
        viewModel?.apply {
          throwInternetError = true
          refreshViewModel(true) {
            Toast.makeText(context, "Finish !", Toast.LENGTH_SHORT).show()
          }
        }
      }
      viewDatabinding?.alertDialog          ->
      {
        displayAlertDialog()
      }
      viewDatabinding?.navigation           ->
      {
        startActivity(Intent(this.context, NavigationActivity::class.java))
      }
    }
  }

  private fun displayAlertDialog()
  {
    context?.let {
      MaterialAlertDialogBuilder(it).apply {
        setTitle("Hi!")
        setMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
        setPositiveButton(android.R.string.ok, null)
        setNegativeButton(android.R.string.cancel, null)
      }.show()
    }
  }

  override fun getRetryView(): View? =
      viewDatabinding?.loadingErrorAndRetry?.retry

}
