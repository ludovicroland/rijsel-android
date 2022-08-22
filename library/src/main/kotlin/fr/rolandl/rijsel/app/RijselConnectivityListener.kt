package fr.rolandl.rijsel.app

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.*
import android.net.wifi.WifiManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import fr.rolandl.rijsel.content.LocalSharedFlowManager
import fr.rolandl.rijsel.lifecycle.RijselLifeCycle
import kotlinx.coroutines.MainScope
import timber.log.Timber

/**
 * A basis class responsible for listening to connectivity events.
 * <p>
 * <b>Caution: this component requires the Android `android.permission.ACCESS_NETWORK_STATE` permission!</b>.
 * </p>
 * <p>
 * This component will issue a broadcast [Intent], every time the hosting application Internet connectivity status changes with the
 * [CONNECTIVITY_CHANGED_ACTION] action, and an extra [HAS_CONNECTIVITY_EXTRA], which states the current application connectivity status.
 * </p>
 * <p>
 * This component should be created during the [fr.rolandl.rijsel.app.RijselApplication.retrieveConnectivityListener] method, and it should be enrolled
 * to all the hosting application activities, during the [fr.rolandl.rijsel.app.RijselApplication.getInterceptor] when
 * receiving the [Lifecycle.Event.ON_CREATE] and [Lifecycle.Event.ON_RESUME] events.
 * </p>
 *
 * @author Ludovic Roland
 * @since 2018.11.06
 */
abstract class RijselConnectivityListener(val context: Context)
  : RijselActivityController.Interceptor
{

  companion object
  {

    /**
     * The action that will used to notify via a broadcast [Intent] when the hosting application Internet connectivity changes.
     */
    const val CONNECTIVITY_CHANGED_ACTION = "connectivityChangedAction"

    /**
     * A broadcast [Intent] boolean flag which indicates the hosting application Internet connectivity status.
     */
    const val HAS_CONNECTIVITY_EXTRA = "hasConnectivityExtra"

  }

  private var hasConnectivity = true

  private var networkCallback: ConnectivityManager.NetworkCallback? = null

  private var activitiesCount: Int = 0

  private val networkStatus by lazy { HashMap<String, Boolean>() }

  init
  {
    activitiesCount = 0

    hasConnectivity = isConnected()

    notifyServices(hasConnectivity)
  }

  protected abstract fun notifyServices(hasConnectivity: Boolean)

  /**
   * This method should be invoked during the [fr.rolandl.rijsel.app.RijselActivityController.Interceptor.onLifeCycleEvent] method, and
   * it will handle everything.
   */
  override fun onLifeCycleEvent(activity: ComponentActivity, fragment: Fragment?, event: RijselLifeCycle.Event)
  {
    if (event == RijselLifeCycle.Event.ON_CREATE)
    {
      // We listen to the network connection potential issues: we do not want child activities to also register for the connectivity change events
      registerBroadcastListener(activity, fragment)
    }
    else if (event == RijselLifeCycle.Event.ON_DESTROY)
    {
      unregisterBroadcastListener(activity, fragment)
    }
  }

  /**
   * @return whether the device has Internet connectivity
   */
  fun hasConnectivity(): Boolean =
      hasConnectivity

  /**
   * @return the currently active network info
   */
  @Deprecated("Please use #getActiveNetwork()", ReplaceWith("getActiveNetwork()"))
  private fun getActiveNetworkInfo(): NetworkInfo? =
      getConnectivityManager().activeNetworkInfo

  @TargetApi(Build.VERSION_CODES.M)
  private fun getActiveNetwork(): Network? =
      getConnectivityManager().activeNetwork

  fun getConnectivityManager(): ConnectivityManager =
      context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  fun getWifiManager(): WifiManager =
      context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private fun registerBroadcastListener(activity: ComponentActivity?, component: Any?)
  {
    // We listen to the network connection potential issues: we do not want child Activities to also register for the connectivity change events
    if (component == null && activity?.parent == null)
    {
      activitiesCount++

      // No need to synchronize this scope, because the method is invoked from the UI thread
      if (networkCallback == null)
      {
        val builder = NetworkRequest.Builder()

        networkCallback = object : ConnectivityManager.NetworkCallback()
        {

          override fun onAvailable(network: Network)
          {
            networkStatus[network.toString()] = true
            onNetworkChangedLollipopAndAbove(networkStatus.containsValue(true))
          }

          override fun onLost(network: Network)
          {
            networkStatus.remove(network.toString())
            onNetworkChangedLollipopAndAbove(networkStatus.containsValue(true))
          }

        }

        networkCallback?.let {
          getConnectivityManager().registerNetworkCallback(builder.build(), it)
        }

        Timber.d("Registered the Lollipop network callback")
      }
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private fun unregisterBroadcastListener(activity: ComponentActivity?, fragment: Fragment?)
  {
    // We listen to the network connection potential issues: we do not want child activities to also register for the connectivity change events
    if (fragment == null && activity?.parent == null)
    {
      activitiesCount--

      if (activitiesCount <= 0)
      {
        networkCallback?.let {
          getConnectivityManager().unregisterNetworkCallback(it)

          Timber.d("Unregisters the Lollipop network callback")

          networkCallback = null
        }
      }
    }
  }

  private fun onNetworkChangedLollipopAndAbove(hasConnectivity: Boolean)
  {
    val previousConnectivity = this.hasConnectivity
    this.hasConnectivity = hasConnectivity

    handleConnectivityChange(previousConnectivity)
  }

  private fun handleConnectivityChange(previousConnectivity: Boolean)
  {
    if (previousConnectivity != hasConnectivity)
    {
      // With this filter, only one broadcast listener will handle the event
      Timber.i("Received an Internet connectivity change event: the connection is now '$hasConnectivity'")

      // We notify the application regarding this connectivity change event
      LocalSharedFlowManager.emit(MainScope(), Intent(RijselConnectivityListener.CONNECTIVITY_CHANGED_ACTION).putExtra(RijselConnectivityListener.HAS_CONNECTIVITY_EXTRA, hasConnectivity))

      notifyServices(hasConnectivity)
    }
  }

  fun isWifiConnected(): Boolean
  {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
    {
      val activeNetworkInfo = getActiveNetworkInfo()

      if (activeNetworkInfo != null)
      {
        if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI && activeNetworkInfo.isConnected == true)
        {
          return true
        }

        return false
      }
    }
    else
    {
      if (getConnectivityManager().getNetworkCapabilities(getActiveNetwork())?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true)
      {
        return true
      }

      return false
    }

    return false
  }

  fun isWifiActivated(): Boolean =
      getWifiManager().isWifiEnabled

  private fun isConnected(): Boolean
  {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
    {
      // We immediately extract the connectivity status
      val isConnected = getActiveNetworkInfo()?.isConnected
      Timber.i("The Internet connection is connected: '$isConnected'")

      return isConnected ?: false
    }
    else
    {
      val networkCapabilities = getConnectivityManager().getNetworkCapabilities(getActiveNetwork()) ?: return false

      return when
      {
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)     -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else                                                                     -> false
      }
    }
  }

}
