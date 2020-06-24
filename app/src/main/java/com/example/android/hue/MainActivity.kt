//TODO - Replace the <fragment> tag with FragmentContainerView.
// (/home/stefan/Documents/tutorials/android/HueRebuild/app/src/main/res/layout/activity_main.xml)

//TODO - Warning:(5, 6) On SDK version 23 and up, your app data will be automatically backed up and
// restored on app install. Consider adding the attribute `android:fullBackupContent` to specify an
// `@xml` resource which configures which files to backup. More info:
// https://developer.android.com/training/backup/autosyncapi.html
// (/home/stefan/Documents/tutorials/android/HueRebuild/app/src/main/AndroidManifest.xml)

//TODO - Warning:(75, 10) Use the Lifecycle Java 8 API provided by the `lifecycle-common-java8`
// library instead of Lifecycle annotations for faster incremental build.
// (/home/stefan/Documents/tutorials/android/HueRebuild/app/build.gradle)

package com.example.android.hue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}