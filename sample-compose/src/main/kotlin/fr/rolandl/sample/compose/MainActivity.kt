package fr.rolandl.sample.compose

import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.rolandl.sample.compose.composable.ActionBar
import fr.rolandl.sample.compose.composable.LoadingErrorAndRetry
import fr.rolandl.sample.compose.theme.SampleTheme
import fr.rolandl.sample.compose.viewmodel.activity.MainActivityViewModel
import fr.rolandl.rijsel.app.RijselConnectivityListener
import fr.rolandl.rijsel.content.LocalSharedFlowManager
import fr.rolandl.rijsel.content.RijselSharedFlowListener
import fr.rolandl.rijsel.content.RijselSharedFlowListenersProvider
import kotlinx.coroutines.launch

/**
 * @author Ludovic Roland
 * @since 2022.04.13
 */
class MainActivity
  : SampleActivity<MainActivityViewModel>(),
    RijselSharedFlowListenersProvider
{

  companion object
  {

    const val MY_ACTION = "myAction"

  }

  override fun getViewModelClass(): Class<MainActivityViewModel> =
    MainActivityViewModel::class.java

  override fun canRotate(): Boolean =
      false

  override fun getSharedFlowListenersCount(): Int =
      3

  override fun getSharedFlowListener(index: Int): RijselSharedFlowListener
  {
    return when (index)
    {
      0    ->
      {
        super.getRijselSharedFlowListener()
      }
      1    ->
      {
        object : RijselSharedFlowListener
        {

          override fun getIntentFilter(): IntentFilter
          {
            return IntentFilter().apply {
              addAction(RijselConnectivityListener.CONNECTIVITY_CHANGED_ACTION)
            }
          }

          override fun onCollect(intent: Intent)
          {
            if (intent.action == RijselConnectivityListener.CONNECTIVITY_CHANGED_ACTION)
            {
              Toast.makeText(this@MainActivity, "has connectivity : '${intent.getBooleanExtra(RijselConnectivityListener.HAS_CONNECTIVITY_EXTRA, false)}'", Toast.LENGTH_LONG).show()
            }
          }
        }
      }
      else ->
      {
        object : RijselSharedFlowListener
        {
          override fun getIntentFilter(): IntentFilter
          {
            return IntentFilter().apply {
              addAction(MainActivity.MY_ACTION)
            }
          }

          override fun onCollect(intent: Intent)
          {
            if (intent.action == MainActivity.MY_ACTION)
            {
              Toast.makeText(this@MainActivity, "Click on Activity !", Toast.LENGTH_SHORT).show()
            }
          }
        }
      }
    }
  }

  @Preview
  @Composable
  override fun Body()
  {
    val state = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()

    SampleTheme {
      Scaffold(
        scaffoldState = state,
        topBar = {
          ActionBar(
            titleRes = R.string.app_name,
            icon = Icons.Filled.Menu
          ) {
            scope.launch {
              if (state.drawerState.isOpen == true) {
                state.drawerState.close()
              } else {
                state.drawerState.open()
              }
            }
          }
        },
        drawerBackgroundColor = colorResource(R.color.design_default_color_error),
        drawerContent = {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .fillMaxHeight()
          )
        },
        content = {
          Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
          ) {
            Content()

            viewModel?.let {
              LoadingErrorAndRetry(viewModel = it)
            }
          }
        }
      )
    }
  }

  @Preview
  @Composable
  private fun Content()
  {
    Box(modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .padding(16.dp)
    ) {
      Column {
        Text(text = "Hello World")
        Button(
          onClick = {
            val intent = Intent(this@MainActivity, SecondActivity::class.java).apply {
              putExtra(SecondActivity.MY_EXTRA, "hey !")
              putExtra(SecondActivity.ANOTHER_EXTRA, "Another Hey !")
            }

            startActivity(intent)
          },
        ) {
          Text(text = "Open another activity!")
        }
        Button(
          onClick = {
            val intent = Intent(this@MainActivity, ThirdActivity::class.java).apply {
              putExtra(ThirdActivity.MY_EXTRA, "go !")
              putExtra(ThirdActivity.ANOTHER_EXTRA, "Another go !")
            }

            startActivity(intent)
          },
        ) {
          Text(text = "Open an activity that supports backstack")
        }
        Button(
          onClick = {
            LocalSharedFlowManager.emit(lifecycleScope, Intent(MainActivity.MY_ACTION))
          },
        ) {
          Text(text = "Click !")
        }
        Button(
          onClick = {
            viewModel?.refreshViewModel(true) {
              Toast.makeText(this@MainActivity, "Finish !", Toast.LENGTH_SHORT).show()
            }
          },
        ) {
          Text(text = "refresh loading")
        }
        Button(
          onClick = {
            viewModel?.refreshViewModel(false) {
              Toast.makeText(this@MainActivity, "Finish !", Toast.LENGTH_SHORT).show()
            }
          },
        ) {
          Text(text = "refresh no loading")
        }
        Button(
          onClick = {
            viewModel?.apply {
              throwError = true
              refreshViewModel(true) {
                Toast.makeText(this@MainActivity, "Finish !", Toast.LENGTH_SHORT).show()
              }
            }
          },
        ) {
          Text(text = "refresh with error")
        }
        Button(
          onClick = {
            viewModel?.apply {
              throwInternetError = true
              refreshViewModel(true) {
                Toast.makeText(this@MainActivity, "Finish !", Toast.LENGTH_SHORT).show()
              }
            }
          },
        ) {
          Text(text = "refresh with internet error")
        }
        Button(
          onClick = {
            displayAlertDialog()
          },
        ) {
          Text(text = "open dialog")
        }
        Button(
          onClick = {
            startActivity(Intent(this@MainActivity, NavigationActivity::class.java))
          },
        ) {
          Text(text = "BottomBar Navigation")
        }
      }
    }
  }

  private fun displayAlertDialog()
  {
    MaterialAlertDialogBuilder(this).apply {
      setTitle("Hi!")
      setMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
      setPositiveButton(android.R.string.ok, null)
      setNegativeButton(android.R.string.cancel, null)
    }.show()
  }

}
