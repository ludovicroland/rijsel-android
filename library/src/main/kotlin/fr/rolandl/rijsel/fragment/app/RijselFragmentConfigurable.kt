package fr.rolandl.rijsel.fragment.app

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import fr.rolandl.rijsel.lifecycle.RijselViewModel

/**
 * Interface which should be used on a [RijselFragment], in order to configure it.
 *
 * @author Ludovic Roland
 * @since 2020.12.18
 */
interface RijselFragmentConfigurable
{

  /**
   * Defines the context which should be attached to the [RijselViewModel] holds by the [RijselFragment].
   */
  enum class ViewModelContext
  {

    /**
     * The [RijselViewModel] should be attached to the fragment
     */
    Fragment,

    /**
     * The [RijselViewModel] should be attached to the activity
     */
    Activity
  }

  /**
   * Defines the behavior of the [RijselViewModel] when configuration of the [RijselFragment] changes.
   */
  enum class ViewModelBehavior
  {

    /**
     * The [RijselViewModel] should reload the compute the ViewModel again
     */
    Reload,

    /**
     * The [RijselViewModel] should not reload the ViewModel if data has been already loaded
     */
    Skip
  }

  /**
   * @return the string identifier to be used on [ActionBar.setTitle].
   */
  @StringRes
  fun fragmentTitleId(): Int? =
      null

  /**
   * @return the string to be used on [ActionBar.setTitle].
   */
  fun fragmentTitle(): String? =
      null

  /**
   * @return the string identifier to be setted on [ActionBar.setSubtitle].
   */
  @StringRes
  fun fragmentSubtitleId(): Int? =
      null

  /**
   * @return the string to be setted on [ActionBar.setSubtitle].
   */
  fun fragmentSubtitle(): String? =
      null

  /**
   * @return the layout identifier to be used in the
   * [RijselFragment.onCreateView] method.
   */
  @LayoutRes
  fun layoutId(): Int? =
      null

  /**
   * @return true if databinding should be done a first time before setting the model
   */
  fun preBind(): Boolean =
      false

  /**
   * @return the context to be attached to the [RijselViewModel].
   */
  fun viewModelContext(): ViewModelContext =
      ViewModelContext.Fragment

  /**
   * @return the behavior to be attached to the [RijselViewModel].
   */
  fun viewModelBehavior(): ViewModelBehavior =
    ViewModelBehavior.Skip

}
