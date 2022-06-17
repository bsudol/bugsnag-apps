//
//  FeatureMgr.swift
//  swift-ios
//
//  Created by Barbara Sudol on 6/9/22.
//  Copyright © 2022 Bugsnag. All rights reserved.
//

import Foundation


// Simple class allowing us to interact with a "Feature Manager" -> when we use the turn_off function to "turn off"
// our feature flags, we disable the code that was causing an exception, allowing the app to successfully launch.
class FeatureMgr {
    static var doWeCrash = true
    static var FF = "new_launch_video"
    
    static func turn_off(msg: String) -> Bool {
        if (msg == FF){
            doWeCrash = false
        }
        return doWeCrash
    }
    
    static func get_active_features() -> String { return FF }
    
    static func generateUncaughtException() {
        let someJson : Dictionary = ["foo":self]
        do {
            let data = try JSONSerialization.data(withJSONObject: someJson, options: .prettyPrinted)
            print("Received data: %@", data)
        } catch {
            // Why does this crash the app? A very good question.
        }
    }
}
