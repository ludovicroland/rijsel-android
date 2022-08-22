package fr.rolandl.sample

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import fr.rolandl.sample.databinding.ActivityThirdBinding
import fr.rolandl.sample.fragment.ThirdFragment
import fr.rolandl.rijsel.appcompat.app.RijselActionBarConfigurable
import fr.rolandl.rijsel.fragment.app.RijselFragment
import timber.log.Timber
import kotlin.reflect.KClass

/**
 * @author Ludovic Roland
 * @since 2018.12.12
 */
class ThirdActivity
  : SampleActivity<ActivityThirdBinding>(),
    FragmentManager.OnBackStackChangedListener
{

  private var drawerToggle: ActionBarDrawerToggle? = null

  override fun inflateViewBinding(): ActivityThirdBinding =
      ActivityThirdBinding.inflate(layoutInflater)

  override fun fragmentPlaceholderId(): Int =
    R.id.fragmentContainer

  override fun fragmentClass(): KClass<out RijselFragment<*, *, *>> =
      ThirdFragment::class

  override fun canRotate(): Boolean =
      true

  override fun actionBarBehavior(): RijselActionBarConfigurable.ActionBarBehavior =
      RijselActionBarConfigurable.ActionBarBehavior.Drawer

  override fun toolbar(): Toolbar? =
      viewBinding?.toolbar

  override fun onCreate(savedInstanceState: Bundle?)
  {
    super.onCreate(savedInstanceState)

    supportFragmentManager.addOnBackStackChangedListener(this)
  }

  override fun onRetrieveDisplayObjects()
  {
    super.onRetrieveDisplayObjects()

    drawerToggle = ActionBarDrawerToggle(this, viewBinding?.drawerLayout, null,
      R.string.app_name,
      R.string.app_name
    )

    drawerToggle?.let {
      viewBinding?.drawerLayout?.addDrawerListener(it)
    }
  }

  override fun onPostCreate(savedInstanceState: Bundle?)
  {
    super.onPostCreate(savedInstanceState)
    drawerToggle?.syncState()
  }

  override fun onBackStackChanged()
  {
    Timber.d("current opened fragment: ${getAggregate()?.openedFragment}")
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean
  {
    if (drawerToggle?.onOptionsItemSelected(item) == true)
    {
      return true
    }

    return super.onOptionsItemSelected(item)
  }

}