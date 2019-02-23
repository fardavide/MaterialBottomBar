import org.gradle.kotlin.dsl.KotlinBuildScript
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.dokka.gradle.DokkaAndroidTask

fun KotlinBuildScript.applyDokka() {
    apply( plugin = "org.jetbrains.dokka-android" )
    configure<DokkaAndroidTask> {
        outputFormat = "html"
        outputDirectory = "docs"
    }
}