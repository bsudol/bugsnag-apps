# Example iOS integration with Bugsnag including Crash Loop/ Feauture Flag Functionality

This app is built off of the [Bugsnag sdk example application](https://github.com/bugsnag/bugsnag-cocoa/tree/master/examples/swift-ios/swift-ios).

- In AppDelegate, we leverage Bugsnag's crash loop detection functionality to check if the app has crashed at launch. 
- The app forces a crash during the launch screen (a.k.a. LaunchView) for the first 2 launch attempts, and the 3rd launch attempt will successfully pass through the launch screen into the login page/ MainView.
- In AppDelegate, if the app has crashed at launch 3 or more times in succession, the FeatureMgr class is leveraged to shut off a "feature", which directly stops the forced crash. The feature flag data will be removed from the session/errors in Bugsnag at this point as well.

1. Open the project root in `/bugsnag-apps/swift-ios` with XCode
2. Insert your API key into the Info.plist
3. Run pod install
4. Run the app! You will need to re-open the app 3x to see a successful launch. Check your Bugsnag dashboard for the NSInvalidArgumentException error which will contain crash loop related breadcrumbs.
