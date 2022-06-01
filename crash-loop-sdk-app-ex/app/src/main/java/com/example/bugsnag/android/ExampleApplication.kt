package com.example.bugsnag.android

import android.annotation.SuppressLint
import android.app.Application
import com.bugsnag.android.Bugsnag
import com.bugsnag.android.Configuration
import com.bugsnag.android.OnErrorCallback
import com.bugsnag.android.okhttp.BugsnagOkHttpPlugin
import com.example.foo.FeatureMgr

class CrashLoopApplication : Application() {

    companion object {
        init {
//            if you support API <= 17 you should uncomment this to load the bugsnag library
//            before any libraries that link to it
//            https://docs.bugsnag.com/platforms/android/#initialize-the-bugsnag-client
//
//            System.loadLibrary("bugsnag-ndk")
//            System.loadLibrary("bugsnag-plugin-android-anr")
            System.loadLibrary("entrypoint")
        }
    }

    private external fun performNativeBugsnagSetup()

    @SuppressLint("LongLogTag")
    override fun onCreate() {
        super.onCreate()
        /////////////////////
        val bugsnagOkHttpPlugin = BugsnagOkHttpPlugin()
        val config = Configuration.load(this)
        config.addPlugin(bugsnagOkHttpPlugin)
        config.persistUser = true
        config.launchDurationMillis = 0

        //See https://docs.bugsnag.com/platforms/android/#identifying-users
        config.addMetadata("account", "subtype", "VIP")
        config.setUser("1415315", "BoJoggs@ex.com", "Bo Joggs")
        /////////////////////

        config.addOnError(OnErrorCallback { event ->
            //Set additional data as seen in https://docs.bugsnag.com/platforms/android/#sending-diagnostic-data
            event.addMetadata("account", "paying_customer", true)

            //Optional: group all crashes of this type together
            if (event.getOriginalError().toString().contains("oops launch crash")) {
                val groupingHash = "1938qehfq34nfq34fq47bfq"
                event.groupingHash = groupingHash
            }
            true
        })

        Bugsnag.start(this, config)

        //Exemplifies pulling active features from a feature manager such as LaunchDarkly/ Split to add to Bugsnag client
        val activeFeatures = FeatureMgr.get_active_features()
        Bugsnag.addFeatureFlag(
            activeFeatures,
            "Active"
        ) //See https://docs.bugsnag.com/platforms/android/features-experiments/


        //Below code is logic to identify and respond to crash loops. See https://docs.bugsnag.com/platforms/android/identifying-crashes-at-launch/
        val crashLoop = Bugsnag.getLastRunInfo()?.consecutiveLaunchCrashes

        if (crashLoop != null) {
            if (Bugsnag.getLastRunInfo()?.crashedDuringLaunch == true && crashLoop == 1) {
                try {
                    getCacheDir().delete(); //Cleared cache after two launch crashes
                    Bugsnag.leaveBreadcrumb("Crash Loop, Cleared Cache") //Log this in dashboard
                    Bugsnag.leaveBreadcrumb("Num Consecutive Crashes = $crashLoop")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (Bugsnag.getLastRunInfo()?.crashedDuringLaunch == true && crashLoop == 2) {
                try {
                    val activeFeatures = FeatureMgr.get_active_features()
                    FeatureMgr.turnoff(activeFeatures) //Cleared active features after third launch crash
                    Bugsnag.clearFeatureFlags()  //Remove active feature flags from upcoming events
                    Bugsnag.leaveBreadcrumb("Crash Loop, Clearing Feature Flags") //Log this in dashboard
                    Bugsnag.leaveBreadcrumb("Num Consecutive Crashes = $crashLoop") //Log this in dashboard
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // Initialise native callbacks
        performNativeBugsnagSetup()
    }
}