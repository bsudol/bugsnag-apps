package com.example.foo;

//Representative of an integrated Feature Flag Manager such as LaunchDarkly, Split.io, etc.
public class FeatureMgr {

    static boolean doWeCrash = true;
    static String FF = "new_launch_video";

    public static boolean turnoff(String msg) {
        if (msg == FF){
            //Set boolean to false so that the exception does not occur during launch.
            //Representative of turning off a feature flag whose code causes an exception at launch.
            doWeCrash = false;
        }
        return (doWeCrash);
    }
    public static String get_active_features() {
        return (FF);
    }
}