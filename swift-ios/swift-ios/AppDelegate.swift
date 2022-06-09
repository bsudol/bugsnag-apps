// Copyright (c) 2016 Bugsnag, Inc. All rights reserved.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall remain in place
// in this source code.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import UIKit
import Bugsnag

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {

        let config = BugsnagConfiguration.loadConfig()
        config.launchDurationMillis = 0
        
        config.addOnSendError { (event) -> Bool in
            event.addMetadata("high", key: "tier", section: "device")
            // return false to stop error from being posted
            return true
        }

        Bugsnag.start(with: config)
        
        //Exemplifies pulling active features from a feature manager such as LaunchDarkly/ Split to add to Bugsnag client
        let activeFeatures = FeatureMgr.get_active_features()
        Bugsnag.addFeatureFlag(name: activeFeatures, variant: "Active") //See https://docs.bugsnag.com/platforms/android/features-experiments/
        
        
        //Below code is logic to identify and respond to crash loops. See https://docs.bugsnag.com/platforms/android/identifying-crashes-at-launch/
        let crashLoop = Bugsnag.lastRunInfo?.consecutiveLaunchCrashes

        if ((crashLoop) != nil){
            if (Bugsnag.lastRunInfo?.crashedDuringLaunch == true && crashLoop! == 1) {
                //clear cache here
                Bugsnag.leaveBreadcrumb(withMessage: "Crash Loop, Cleared Cache")
                Bugsnag.leaveBreadcrumb("Crash Loop", metadata: ["value": crashLoop], type: .log)
            }
            if (Bugsnag.lastRunInfo?.crashedDuringLaunch == true && crashLoop! == 2) {
                FeatureMgr.turn_off(msg: activeFeatures)
                Bugsnag.clearFeatureFlags()
                Bugsnag.leaveBreadcrumb("Cleared Feature Flags", metadata: ["value": activeFeatures], type: .log)
                Bugsnag.leaveBreadcrumb("Crash Loop", metadata: ["value": crashLoop], type: .log)
            }
        }
        
        
        do {
            sleep(1)
            if (FeatureMgr.turn_off(msg: "")) {
                FeatureMgr.generateUncaughtException()
            }
        }
            
        Bugsnag.markLaunchCompleted()
        return true
    }
    
    @available(iOS 13.0, *)
    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }
}
