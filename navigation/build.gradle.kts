plugins {
    id("com.android.library" )
    id("kotlin-android" )
    id("kotlin-android-extensions" )
    id("org.jetbrains.dokka-android" )
}

android { applyAndroidConfig() }

dependencies {
    implementation( project(":materialbottombar" ) )

    implementation( Libs.Android.navigation_fragment )
    implementation( Libs.Android.navigation_ui )
}

applyDokka()
publish("materialbottombar-navigation" )