package fr.rolandl.sample.compose.fragment

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.rolandl.rijsel.content.RijselSharedFlowListener
import fr.rolandl.rijsel.content.RijselSharedFlowListenerProvider
import fr.rolandl.rijsel.fragment.app.RijselComposeFragmentConfigurable
import fr.rolandl.sample.compose.composable.LoadingErrorAndRetry
import fr.rolandl.sample.compose.theme.SampleTheme
import fr.rolandl.sample.compose.viewmodel.fragment.FirstNavigationFragmentViewModel

/**
 * @author Ludovic Roland
 * @since 2022.04.14
 */
class FirstNavigationFragment
  : SampleFragment<FirstNavigationFragmentViewModel>(),
    RijselSharedFlowListenerProvider
{

  override fun getViewModelClass(): Class<FirstNavigationFragmentViewModel> =
      FirstNavigationFragmentViewModel::class.java

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

  @OptIn(ExperimentalLifecycleComposeApi::class)
  @Composable
  override fun Body()
  {
    SampleTheme {
      Scaffold (
        content = {
          Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(it)) {
            Text(
              modifier = Modifier.align(Alignment.Center),
              text = viewModel?.counter?.collectAsStateWithLifecycle()?.value?.toString() ?: ""
            )

            viewModel?.let {
              LoadingErrorAndRetry(viewModel = it)
            }
          }
        }
      )
    }
  }

}
