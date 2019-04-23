@file:Suppress("MayBeConstant")

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.ScriptHandlerScope
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.maven

val repos: RepositoryHandler.() -> Unit get() = {
    google()
    jcenter()
    maven("https://kotlin.bintray.com/kotlinx" )
}

val ScriptHandlerScope.classpathDependencies: DependencyHandlerScope.() -> Unit get() = {
    classpath( kotlin("gradle-plugin", Versions.kotlin ) )
    classpath( Libs.dokka )
    classpath( Libs.Android.gradle_plugin )
    classpath( Libs.Android.navigation_plugin )
    classpath( Libs.Publishing.bintray_plugin )
    classpath( Libs.Publishing.maven_plugin )
}

@Suppress("unused")
fun DependencyHandler.applyAndroidTests() {
    Libs.run {
        listOf( test, test_junit, mockk_android )
            .forEach { add("androidTestImplementation", it ) }
    }
    Libs.Android.run {
        listOf( espresso, test_runner )
            .forEach { add( "androidTestImplementation", it ) }
    }
}

object Versions {
    val kotlin =                        "1.3.30"
    val dokka =                         "0.9.18"
    val mockk =                         "1.9.3"

    val android_constraint_layout =     "2.0.0-alpha4"
    val android_espresso =              "3.1.1"
    //val android_glide =                 "4.9.0"
    val android_gradle_plugin =         "3.3.1"
    val android_ktx =                   "1.1.0-alpha05"
    val android_material =              "1.1.0-alpha05"
    val android_navigation =            "2.0.0"
    val android_test_runner =           "1.1.1"
    val android_theia =                 "0.3-alpha-6"

    val publishing_bintray_plugin =     "1.8.4"
    val publishing_maven_plugin =       "2.1"
}

@Suppress("unused")
object Libs {

    /* Kotlin */
    val kotlin =                                "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val test =                                  "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
    val test_junit =                            "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"

    val dokka =                                 "org.jetbrains.dokka:dokka-android-gradle-plugin:${Versions.dokka}"
    val mockk =                                 "io.mockk:mockk:${Versions.mockk}"
    val mockk_android =                         "io.mockk:mockk-android:${Versions.mockk}"

    /* Android */
    object Android {
        val constraint_layout =                 "androidx.constraintlayout:constraintlayout:${Versions.android_constraint_layout}"
        val espresso =                          "androidx.test.espresso:espresso-core:${Versions.android_espresso}"
        //val glide =                             "com.github.bumptech.glide:glide:${Versions.android_glide}"
        //val glide_compiler =                    "com.github.bumptech.glide:compiler:${Versions.android_glide}"
        val gradle_plugin =                     "com.android.tools.build:gradle:${Versions.android_gradle_plugin}"
        val ktx =                               "androidx.core:core-ktx:${Versions.android_ktx}"
        val material =                          "com.google.android.material:material:${Versions.android_material}"
        val navigation_fragment =               "androidx.navigation:navigation-fragment-ktx:${Versions.android_navigation}"
        val navigation_plugin =                 "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.android_navigation}"
        val navigation_ui =                     "androidx.navigation:navigation-ui-ktx:${Versions.android_navigation}"
        val support_annotations =               "com.android.support:support-annotations:28.0.0"
        val test_runner =                       "com.android.support.test:runner:${Versions.android_test_runner}"
        val theia =                             "studio.forface.theia:theia:${Versions.android_theia}"
    }

    /* Publishing */
    object Publishing {
        val bintray_plugin =                    "com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.publishing_bintray_plugin}"
        val maven_plugin =                      "com.github.dcendents:android-maven-gradle-plugin:${Versions.publishing_maven_plugin}"
    }
}