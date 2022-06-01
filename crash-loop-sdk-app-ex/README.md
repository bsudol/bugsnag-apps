# Example Android integration with Bugsnag including Crash Loop/ Feauture Flag Functionality

This app is built off of the [Bugsnag sdk example application](https://github.com/bugsnag/bugsnag-android/tree/next/examples/sdk-app-example).
- In ExampleApplication, we leverage Bugsnag's crash loop detection functionality to check if the app has crashed at launch. 
- The app forces a crash during the launch screen (a.k.a. SplashScreen) for the first 3 launch attempts, and the 4th launch attempt will successfully pass through the splash screen into the login page.
- In ExampleApplication, if the app has crashed at launch 3 or more times in succession, the FeatureMgr class is leveraged to shut off a "feature", which directly turns off the forced crash. The feature flag data will be removed from the session/errors in Bugsnag at this point as well.

1. Open the project root in `/example-projects/Android/crash-loop-sdk-app-example` with Android Studio
2. Insert your API key into the AndroidManifest.xml
3. Run the app! You will need to re-open the app 3x to see a successful launch. Check your Bugsnag dashboard for the "oops launch crash" error which will contain crash loop related breadcrumbs.