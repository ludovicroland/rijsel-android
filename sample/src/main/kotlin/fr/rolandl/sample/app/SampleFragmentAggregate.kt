package fr.rolandl.sample.app

import androidx.fragment.app.Fragment
import fr.rolandl.rijsel.fragment.app.RijselFragmentAggregate
import fr.rolandl.rijsel.fragment.app.RijselFragmentConfigurable

/**
 * @author Ludovic Roland
 * @since 2018.11.08
 */
class SampleFragmentAggregate(fragment: Fragment, fragmentConfigurable: RijselFragmentConfigurable?)
  : RijselFragmentAggregate(fragment, fragmentConfigurable)