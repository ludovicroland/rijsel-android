package fr.rolandl.rijsel.fragment.app

import fr.rolandl.rijsel.lifecycle.RijselComposeViewModel

/**
 * Interface which should be used on a [RijselComposeFragment], in order to configure it.
 *
 * @author Ludovic Roland
 * @since 2022.04.12
 */
interface RijselComposeFragmentConfigurable
{

  /**
   * Defines the context which should be attached to the [RijselComposeViewModel] holds by the [RijselComposeFragment].
   */
  enum class ViewModelContext
  {

    /**
     * The [RijselComposeViewModel] should be attached to the fragment
     */
    Fragment,

    /**
     * The [RijselComposeViewModel] should be attached to the activity
     */
    Activity
  }

  /**
   * Defines the behavior of the [RijselComposeViewModel] when configuration of the [RijselFragment] changes.
   */
  enum class ViewModelBehavior
  {

    /**
     * The [RijselComposeViewModel] should reload the compute the ViewModel again
     */
    Reload,

    /**
     * The [RijselComposeViewModel] should not reload the ViewModel if data has been already loaded
     */
    Skip
  }

  /**
   * @return the context to be attached to the [RijselComposeViewModel].
   */
  fun viewModelContext(): ViewModelContext =
      ViewModelContext.Fragment

  /**
   * @return the behavior to be attached to the [RijselComposeViewModel].
   */
  fun viewModelBehavior(): ViewModelBehavior =
    ViewModelBehavior.Skip

}
