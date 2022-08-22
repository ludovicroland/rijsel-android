package fr.rolandl.sample.fragment

import android.view.View
import fr.rolandl.sample.R
import fr.rolandl.sample.databinding.FragmentMenuBinding
import fr.rolandl.rijsel.lifecycle.DummyRijselViewModel

/**
 * @author Ludovic Roland
 * @since 2018.11.08
 */
class MenuFragment
  : SampleFragment<FragmentMenuBinding, DummyRijselViewModel>()
{

  override fun layoutId(): Int =
      R.layout.fragment_menu

  override fun getRetryView(): View? =
      null

}
