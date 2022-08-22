package fr.rolandl.sample.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import fr.rolandl.sample.R
import fr.rolandl.sample.SampleActivity
import fr.rolandl.sample.adapter.MyAdapter
import fr.rolandl.sample.databinding.FragmentThirdBinding
import fr.rolandl.sample.viewmodel.ThirdFragmentViewModel
import fr.rolandl.rijsel.appcompat.app.RijselActivityAggregate
import java.util.*

/**
 * @author Ludovic Roland
 * @since 2018.12.12
 */
class ThirdFragment
  : SampleFragment<FragmentThirdBinding, ThirdFragmentViewModel>(),
    View.OnClickListener
{

  companion object
  {

    const val MY_EXTRA = "myExtra"

    const val ANOTHER_EXTRA = "anotherExtra"

  }

  override fun layoutId(): Int =
      R.layout.fragment_third

  override fun fragmentTitleId(): Int =
      R.string.app_name

  override fun getViewModelClass(): Class<ThirdFragmentViewModel> =
      ThirdFragmentViewModel::class.java


  override fun onViewCreated(view: View, savedInstanceState: Bundle?)
  {
    super.onViewCreated(view, savedInstanceState)

    viewDatabinding?.refreshError?.setOnClickListener(this)
    viewDatabinding?.refreshInternetError?.setOnClickListener(this)
    viewDatabinding?.observableField?.setOnClickListener(this)
    viewDatabinding?.backstack?.setOnClickListener(this)

    viewDatabinding?.list?.setHasFixedSize(true)
  }

  override fun onLoadedState()
  {
    super.onLoadedState()

    viewDatabinding?.list?.adapter = MyAdapter(viewModel?.persons ?: emptyList())
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
        (viewModel as ThirdFragmentViewModel).anotherString.postValue(UUID.randomUUID().toString())
      }
      viewDatabinding?.backstack            ->
      {
        (activity as? SampleActivity<*>)?.getAggregate()?.addOrReplaceFragment(BackstackFragment::class, R.id.fragmentContainer, true, "BackstackFragment", null, null, RijselActivityAggregate.FragmentTransactionType.Add)
      }
    }
  }

  override fun getRetryView(): View? =
      viewDatabinding?.loadingErrorAndRetry?.retry

}
