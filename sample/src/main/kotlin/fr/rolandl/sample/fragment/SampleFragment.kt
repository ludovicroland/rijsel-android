package fr.rolandl.sample.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import fr.rolandl.sample.app.SampleFragmentAggregate
import fr.rolandl.rijsel.fragment.app.RijselFragment
import fr.rolandl.rijsel.fragment.app.RijselFragmentAggregate
import fr.rolandl.rijsel.fragment.app.RijselFragmentConfigurable
import fr.rolandl.rijsel.lifecycle.RijselViewModel

/**
 * @author Ludovic Roland
 * @since 2018.11.08
 */
abstract class SampleFragment<BindingClass : ViewDataBinding, ViewModelClass : RijselViewModel>
  : RijselFragment<SampleFragmentAggregate, BindingClass, ViewModelClass>(),
    RijselFragmentAggregate.OnBackPressedListener, RijselFragmentConfigurable
{

  abstract fun getRetryView(): View?

  override fun onViewCreated(view: View, savedInstanceState: Bundle?)
  {
    super.onViewCreated(view, savedInstanceState)

    getRetryView()?.setOnClickListener {
      viewModel?.refreshViewModel(true, null)
    }
  }

  override fun getBindingVariable(): Int =
    fr.rolandl.sample.BR.model

  override fun onBackPressed(): Boolean =
      false

}
