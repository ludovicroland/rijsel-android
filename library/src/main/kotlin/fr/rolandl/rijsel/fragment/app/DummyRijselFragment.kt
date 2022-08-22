package fr.rolandl.rijsel.fragment.app

import androidx.annotation.RestrictTo
import androidx.databinding.ViewDataBinding
import fr.rolandl.rijsel.lifecycle.DummyRijselViewModel

/**
 * TODO
 *
 * @author Ludovic Roland
 * @since 2018.11.07
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class DummyRijselFragment
  : RijselFragment<DummyRijselFragmentAggregate, ViewDataBinding, DummyRijselViewModel>()
{

  override fun getBindingVariable(): Int? =
      null

  override fun getViewModelClass(): Class<DummyRijselViewModel> =
      DummyRijselViewModel::class.java

}
