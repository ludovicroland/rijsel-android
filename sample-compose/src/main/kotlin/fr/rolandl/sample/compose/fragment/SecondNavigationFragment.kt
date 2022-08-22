package fr.rolandl.sample.compose.fragment

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import fr.rolandl.sample.compose.composable.LoadingErrorAndRetry
import fr.rolandl.sample.compose.theme.SampleTheme
import fr.rolandl.sample.compose.viewmodel.fragment.SecondNavigationFragmentViewModel
import fr.rolandl.rijsel.content.RijselSharedFlowListener
import fr.rolandl.rijsel.content.RijselSharedFlowListenerProvider
import fr.rolandl.rijsel.fragment.app.RijselComposeFragmentConfigurable
import timber.log.Timber

/**
 * @author Ludovic Roland
 * @since 2022.04.14
 */
class SecondNavigationFragment :
    SampleFragment<SecondNavigationFragmentViewModel>(),
    RijselSharedFlowListenerProvider
{
  override fun getViewModelClass(): Class<SecondNavigationFragmentViewModel> =
      SecondNavigationFragmentViewModel::class.java

  override fun viewModelBehavior(): RijselComposeFragmentConfigurable.ViewModelBehavior =
    RijselComposeFragmentConfigurable.ViewModelBehavior.Reload

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

    Toast.makeText(requireContext(), getIdentityHashCode(), Toast.LENGTH_LONG).show()
  }

  override fun onPause()
  {
    Timber.d("onPause")
    super.onPause()
  }

  override fun onStop()
  {
    Timber.d("onStop")
    super.onStop()
  }

  override fun onDestroy()
  {
    Timber.d("onDestroy")
    super.onDestroy()
  }

  override fun onDestroyView()
  {
    Timber.d("onDestroyView")
    super.onDestroyView()
  }

  private fun getIdentityHashCode() =
      System.identityHashCode(this).toString()

  @Composable
  override fun Body()
  {
    val theItems = viewModel?.items?.collectAsState()?.value ?: listOf()

    SampleTheme {
      Scaffold (
        content = {
          Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            LazyColumn(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
              items(theItems) {
                Text(text = "${it.text} - ${it.fragmentIdentityHashCode}")
              }
            }
            viewModel?.let {
              LoadingErrorAndRetry(viewModel = it)
            }
          }
        }
      )
    }
  }

}
