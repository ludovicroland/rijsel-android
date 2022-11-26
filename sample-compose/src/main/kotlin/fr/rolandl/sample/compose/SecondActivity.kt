package fr.rolandl.sample.compose

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.rolandl.sample.compose.composable.DefaultBody
import fr.rolandl.sample.compose.viewmodel.activity.SecondActivityViewModel

/**
 * @author Ludovic Roland
 * @since 2022.04.14
 */
class SecondActivity
  : SampleActivity<SecondActivityViewModel>()
{

  companion object
  {

    const val MY_EXTRA = "myExtra"

    const val ANOTHER_EXTRA = "anotherExtra"

  }

  override fun getViewModelClass(): Class<SecondActivityViewModel> =
    SecondActivityViewModel::class.java

  override fun canRotate(): Boolean =
    true

  @Preview
  @Composable
  override fun Body()
  {
    DefaultBody(
      viewModel = viewModel,
      titleRes = R.string.app_name,
      icon = Icons.Filled.ArrowBack,
      onIconClicked = { onBackPressed() },
      content = {
        Content()
      }
    )
  }

  @OptIn(ExperimentalLifecycleComposeApi::class)
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
        Text(text = viewModel?.myString ?: "")
        Text(text = viewModel?.anotherString?.collectAsStateWithLifecycle()?.value ?: "")
        Button(
          onClick = {
            viewModel?.refreshViewModel(true) {
              Toast.makeText(this@SecondActivity, "Finish !", Toast.LENGTH_SHORT).show()
            }
          },
        ) {
          Text(text = "refresh loading")
        }
        Button(
          onClick = {
            viewModel?.refreshViewModel(false) {
              Toast.makeText(this@SecondActivity, "Finish !", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@SecondActivity, "Finish !", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@SecondActivity, "Finish !", Toast.LENGTH_SHORT).show()
              }
            }
          },
        ) {
          Text(text = "refresh with internet error")
        }
        Button(
          onClick = {
            viewModel?.updateStateFlow()
          },
        ) {
          Text(text = "MutableStateFlow field")
        }
      }
    }
  }

}
