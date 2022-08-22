package fr.rolandl.sample

import fr.rolandl.sample.databinding.ActivitySecondBinding
import fr.rolandl.sample.fragment.SecondFragment
import fr.rolandl.rijsel.appcompat.app.RijselActionBarConfigurable
import fr.rolandl.rijsel.fragment.app.RijselFragment
import kotlin.reflect.KClass

/**
 * @author Ludovic Roland
 * @since 2018.12.05
 */
class SecondActivity
  : SampleActivity<ActivitySecondBinding>()
{

  override fun inflateViewBinding(): ActivitySecondBinding =
      ActivitySecondBinding.inflate(layoutInflater)

  override fun fragmentPlaceholderId(): Int =
    R.id.fragmentContainer

  override fun fragmentClass(): KClass<out RijselFragment<*, *, *>> =
      SecondFragment::class

  override fun canRotate(): Boolean =
      true

  override fun actionBarBehavior(): RijselActionBarConfigurable.ActionBarBehavior =
      RijselActionBarConfigurable.ActionBarBehavior.Up

}
