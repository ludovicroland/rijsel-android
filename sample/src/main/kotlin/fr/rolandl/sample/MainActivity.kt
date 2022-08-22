package fr.rolandl.sample

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import fr.rolandl.sample.databinding.ActivityMainBinding
import fr.rolandl.sample.fragment.MainFragment
import fr.rolandl.rijsel.app.RijselConnectivityListener
import fr.rolandl.rijsel.appcompat.app.RijselActionBarConfigurable
import fr.rolandl.rijsel.content.RijselSharedFlowListener
import fr.rolandl.rijsel.content.RijselSharedFlowListenersProvider
import fr.rolandl.rijsel.fragment.app.RijselFragment
import kotlin.reflect.KClass

/**
 * @author Ludovic Roland
 * @since 2018.11.08
 */
class MainActivity
  : SampleActivity<ActivityMainBinding>(),
    RijselSharedFlowListenersProvider
{

  private var drawerToggle: ActionBarDrawerToggle? = null

  override fun inflateViewBinding(): ActivityMainBinding =
      ActivityMainBinding.inflate(layoutInflater)

  override fun fragmentPlaceholderId(): Int =
    R.id.fragmentContainer

  override fun fragmentClass(): KClass<out RijselFragment<*, *, *>> =
      MainFragment::class

  override fun canRotate(): Boolean =
      false

  override fun actionBarBehavior(): RijselActionBarConfigurable.ActionBarBehavior =
      RijselActionBarConfigurable.ActionBarBehavior.Drawer

  override fun getSharedFlowListenersCount(): Int =
      3

  override fun getSharedFlowListener(index: Int): RijselSharedFlowListener
  {
    return when (index)
    {
      0    ->
      {
        super.getRijselSharedFlowListener()
      }
      1    ->
      {
        object : RijselSharedFlowListener
        {

          override fun getIntentFilter(): IntentFilter
          {
            return IntentFilter().apply {
              addAction(RijselConnectivityListener.CONNECTIVITY_CHANGED_ACTION)
            }
          }

          override fun onCollect(intent: Intent)
          {
            if (intent.action == RijselConnectivityListener.CONNECTIVITY_CHANGED_ACTION)
            {
              Toast.makeText(this@MainActivity, "has connectivity : '${intent.getBooleanExtra(RijselConnectivityListener.HAS_CONNECTIVITY_EXTRA, false)}'", Toast.LENGTH_LONG).show()
            }
          }
        }
      }
      else ->
      {
        object : RijselSharedFlowListener
        {
          override fun getIntentFilter(): IntentFilter
          {
            return IntentFilter().apply {
              addAction(MainFragment.MY_ACTION)
              addCategory(MainActivity::class.java.simpleName)
            }
          }

          override fun onCollect(intent: Intent)
          {
            if (intent.action == MainFragment.MY_ACTION)
            {
              Toast.makeText(this@MainActivity, "Click on Activity !", Toast.LENGTH_SHORT).show()
            }
          }
        }
      }
    }
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

  override fun onOptionsItemSelected(item: MenuItem): Boolean
  {
    if (drawerToggle?.onOptionsItemSelected(item) == true)
    {
      return true
    }

    return super.onOptionsItemSelected(item)
  }

}
