plugins {
    id("com.android.library" )
    id("kotlin-android" )
    id("kotlin-android-extensions" )
}

android { applyAndroidConfig() }

dependencies {
    api( Libs.kotlin )

    implementation( Libs.Android.constraint_layout )
    api( Libs.Android.ktx )
    implementation( Libs.Android.material )
    implementation( Libs.Android.theia )
}

applyDokka()
publish("materialbottombar" )