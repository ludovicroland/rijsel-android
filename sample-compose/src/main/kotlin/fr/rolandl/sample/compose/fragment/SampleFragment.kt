package fr.rolandl.sample.compose.fragment

import fr.rolandl.sample.compose.app.SampleFragmentAggregate
import fr.rolandl.rijsel.fragment.app.RijselComposeFragment
import fr.rolandl.rijsel.lifecycle.RijselComposeViewModel

/**
 * @author Ludovic Roland
 * @since 2022.04.13
 */
abstract class SampleFragment<ViewModelClass : RijselComposeViewModel>
  : RijselComposeFragment<SampleFragmentAggregate, ViewModelClass>()