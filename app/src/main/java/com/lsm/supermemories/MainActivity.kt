package com.lsm.supermemories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mNotificationTime = Calendar.getInstance().timeInMillis + 5000 //Set after 5 seconds from the current time.
    private var mNotified = false
    private lateinit var navController: NavController
    private var interstitialAd: InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        interstitialAd = newInterstitialAd()
        loadInterstitial()


        var navController = findNavController(R.id.nav_host_fragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    private fun newInterstitialAd(): InterstitialAd {
        return InterstitialAd(this).apply {
            adUnitId = getString(R.string.interstitial_ad_unit_id)
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    interstitialAd?.show()
                    super.onAdLoaded()
                    //    nextLevelButton.isEnabled = true
                }


            }
        }


    }
    private fun loadInterstitial() {
        println("loadInteresitial")
        // Disable the next level button and load the ad.

            val adRequest = AdRequest.Builder()
                    .setRequestAgent("android_studio:ad_template")
                    .build()
            interstitialAd?.loadAd(adRequest)

        }



}