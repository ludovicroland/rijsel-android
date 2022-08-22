package fr.rolandl.sample.compose

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import fr.rolandl.sample.compose.app.SampleAppCompatActivityAggregate
import fr.rolandl.sample.compose.databinding.ActivityNavigationBinding
import fr.rolandl.rijsel.appcompat.app.RijselAppCompatActivity

/**
 * @author Ludovic Roland
 * @since 2022.04.14
 */
class NavigationActivity
  : RijselAppCompatActivity<SampleAppCompatActivityAggregate, ActivityNavigationBinding>()
{
  private var navController: NavController? = null

  override fun inflateViewBinding(): ActivityNavigationBinding =
    ActivityNavigationBinding.inflate(layoutInflater)

  override fun onCreate(savedInstanceState: Bundle?)
  {
    super.onCreate(savedInstanceState)

    navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController.also {
      viewBinding?.navView?.setupWithNavController(it)
    }
  }

}
