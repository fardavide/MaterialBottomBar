plugins {
    id("com.android.application" )
    id("kotlin-android" )
    id("kotlin-android-extensions" )
}

android { applyAndroidConfig("studio.forface.materialbottombar.demo" ) }

dependencies {
    implementation( project(":materialbottombar" ) )
    implementation( project(":navigation" ) )

    implementation( Libs.kotlin )

    implementation( Libs.Android.constraint_layout )
    implementation( Libs.Android.ktx )
    implementation( Libs.Android.material )
    implementation( Libs.Android.navigation_fragment )
    implementation( Libs.Android.navigation_ui )
}