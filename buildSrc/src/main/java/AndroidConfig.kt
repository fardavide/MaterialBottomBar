import com.android.build.gradle.TestedExtension

@Suppress("unused")
fun TestedExtension.applyAndroidConfig( appId: String? = null ) {
    compileSdkVersion( Project.targetSdk )
    defaultConfig {
        appId?.let { applicationId = it }
        minSdkVersion( Project.minSdk )
        targetSdkVersion( Project.targetSdk )
        versionCode = Project.versionCode
        versionName = Project.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        getByName( "release" ) {
            isMinifyEnabled = false
            proguardFiles( getDefaultProguardFile("proguard-android.txt" ), "proguard-rules.pro" )
        }
        getByName("debug" ) {}
    }
    sourceSets {
        getByName("main" ).java.srcDirs("src/main/kotlin" )
        getByName("test" ).java.srcDirs("src/test/kotlin" )
        getByName("androidTest" ).java.srcDirs("src/androidTest/kotlin" )
    }
    compileOptions {
        sourceCompatibility = Project.jdkVersion
        targetCompatibility = Project.jdkVersion
    }
    packagingOptions {
        exclude("META-INF/DEPENDENCIES" )
        exclude("META-INF/LICENSE" )
        exclude("META-INF/LICENSE.txt" )
        exclude("META-INF/license.txt" )
        exclude("META-INF/NOTICE" )
        exclude("META-INF/NOTICE.txt" )
        exclude("META-INF/notice.txt" )
        exclude("META-INF/ASL2.0" )
        exclude("META-INF/ktor-http.kotlin_module" )
        exclude("META-INF/kotlinx-io.kotlin_module" )
        exclude("META-INF/atomicfu.kotlin_module" )
        exclude("META-INF/ktor-utils.kotlin_module" )
        exclude("META-INF/kotlinx-coroutines-io.kotlin_module" )
        exclude("META-INF/ktor-client-json.kotlin_module" )
        exclude("META-INF/ktor-client-logging.kotlin_module" )
        exclude("META-INF/ktor-client-core.kotlin_module" )
    }
}