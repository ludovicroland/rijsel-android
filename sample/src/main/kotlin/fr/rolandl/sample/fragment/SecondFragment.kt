package fr.rolandl.sample.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import fr.rolandl.sample.R
import fr.rolandl.sample.databinding.FragmentSecondBinding
import fr.rolandl.sample.viewmodel.SecondFragmentViewModel
import fr.rolandl.rijsel.fragment.app.RijselFragmentConfigurable
import java.util.*

/**
 * @author Ludovic Roland
 * @since 2018.11.08
 */
class SecondFragment
  : SampleFragment<FragmentSecondBinding, SecondFragmentViewModel>(),
    View.OnClickListener
{

  companion object
  {

    const val MY_EXTRA = "myExtra"

    const val ANOTHER_EXTRA = "anotherExtra"

  }

  override fun layoutId(): Int =
      R.layout.fragment_second

  override fun fragmentTitleId(): Int =
      R.string.app_name

  override fun getViewModelClass(): Class<SecondFragmentViewModel> =
      SecondFragmentViewModel::class.java

  override fun viewModelBehavior(): RijselFragmentConfigurable.ViewModelBehavior =
    RijselFragmentConfigurable.ViewModelBehavior.Reload

  override fun onViewCreated(view: View, savedInstanceState: Bundle?)
  {
    super.onViewCreated(view, savedInstanceState)

    viewDatabinding?.refreshError?.setOnClickListener(this)
    viewDatabinding?.refreshInternetError?.setOnClickListener(this)
    viewDatabinding?.observableField?.setOnClickListener(this)
  }

  override fun onClick(view: View?)
  {
    when (view)
    {
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
      viewDatabinding?.observableField      ->
      {
        viewModel?.anotherString?.postValue(UUID.randomUUID().toString())
      }
    }
  }

  override fun getRetryView(): View? =
      viewDatabinding?.loadingErrorAndRetry?.retry

}
