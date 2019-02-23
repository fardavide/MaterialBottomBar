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
    api( Libs.kotlin )

    implementation( Libs.Android.constraint_layout )
    implementation( Libs.Android.glide )
    api( Libs.Android.ktx )
    implementation( Libs.Android.material )
}

publish("materialbottombar" )