package fr.rolandl.sample

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import fr.rolandl.sample.databinding.ActivityNavigationBinding
import fr.rolandl.sample.viewmodel.NavigationActivityViewModel

/**
 * @author Ludovic Roland
 * @since 2022/03/22
 */
class NavigationActivity
  : SampleActivity<ActivityNavigationBinding>()
{
  private val model: NavigationActivityViewModel by viewModels()

  private var navController: NavController? = null

  override fun inflateViewBinding(): ActivityNavigationBinding =
      ActivityNavigationBinding.inflate(layoutInflater)

  override fun onCreate(savedInstanceState: Bundle?)
  {
    super.onCreate(savedInstanceState)

    navController = (supportFragmentManager.findFragmentById(R.id.navigation_fragment) as NavHostFragment).navController.also {
      viewBinding?.navigationView?.setupWithNavController(it)
    }

//    viewBinding?.navigationView?.setOnItemSelectedListener { item ->
//      if(item.itemId == R.id.first)
//      {
//        getAggregate()?.replaceFragment(FirstNavigationFragment::class, R.id.navigation_fragment, false, "")
//      }
//      else
//      {
//        getAggregate()?.replaceFragment(SecondNavigationFragment::class, R.id.navigation_fragment, false, "")
//      }
//      true
//    }
  }
}