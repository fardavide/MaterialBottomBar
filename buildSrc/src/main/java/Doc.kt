import org.gradle.kotlin.dsl.KotlinBuildScript
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaAndroidTask

@Suppress("unused")
fun KotlinBuildScript.applyDokka() {
    apply( plugin = "com.android.library" )
    apply( plugin = "kotlin-android" )
    apply( plugin = "org.jetbrains.dokka-android" )
    tasks.withType( DokkaAndroidTask::class ) {
        outputFormat = "html"
        outputDirectory = "docs"
    }
}