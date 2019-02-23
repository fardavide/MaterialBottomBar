plugins {
    id("com.android.library" )
    id("kotlin-android" )
    id("kotlin-android-extensions" )
    id("org.jetbrains.dokka-android" )
}

android { applyAndroidConfig() }

//dokka { TODO
//    outputFormat = 'html'
//    outputDirectory = "$buildDir/javadoc"
//}

dependencies {
    implementation( project(":materialbottombar" ) )

    implementation( Libs.Android.navigation_fragment )
    implementation( Libs.Android.navigation_ui )
}

publish("materialbottombar-navigation" )