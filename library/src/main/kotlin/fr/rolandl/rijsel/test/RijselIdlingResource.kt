//package fr.rolandl.rijsel.test
//
//import androidx.test.espresso.idling.CountingIdlingResource
//
///**
// * @author Ludovic Roland
// * @since 2022.04.28
// */
//object RijselIdlingResource
//{
//
//  private const val NAME = "RIJSEL_IDLING_RESOURCE"
//
//  @JvmField
//  val countingIdlingResource = CountingIdlingResource(RijselIdlingResource.NAME)
//
//  fun increment()
//  {
//    countingIdlingResource.increment()
//  }
//
//  fun decrement()
//  {
//    if (countingIdlingResource.isIdleNow == false)
//    {
//      countingIdlingResource.decrement()
//    }
//  }
//
//}
