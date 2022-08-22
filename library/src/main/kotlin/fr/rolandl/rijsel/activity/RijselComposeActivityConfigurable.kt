package fr.rolandl.rijsel.activity

import fr.rolandl.rijsel.fragment.app.RijselComposeFragmentConfigurable
import fr.rolandl.rijsel.lifecycle.RijselComposeViewModel

/**
 * Interface used on a [RijselComposeActivity], in order to configure the [androidx.compose.material.Scaffold] content.
 * <p>
 * The associated [RijselComposeActivityAggregate] will configure the behavior of the [androidx.compose.material.Scaffold].
 * </p>
 *
 * @author Ludovic Roland
 * @see RijselComposeActivityAggregate
 * @since 2022.04.11
 */
interface RijselComposeActivityConfigurable
{

  /**
   * Defines the behavior of the [RijselComposeViewModel] when configuration of the [RijselComposeActivity] changes.
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
   * @return true if the activity can rotate.
   */
  fun canRotate(): Boolean =
    false

  /**
   * @return the behavior to be attached to the [RijselComposeViewModel].
   */
  fun viewModelBehavior(): RijselComposeFragmentConfigurable.ViewModelBehavior =
    RijselComposeFragmentConfigurable.ViewModelBehavior.Skip

}
