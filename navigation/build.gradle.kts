plugins {
    id("com.android.library" )
    id("kotlin-android" )
    id("kotlin-android-extensions" )
}

android { applyAndroidConfig() }

dependencies {
    implementation( project(":materialbottombar" ) )

    implementation( Libs.Android.navigation_fragment )
    implementation( Libs.Android.navigation_ui )
}

applyDokka()
publish("materialbottombar-navigation" )