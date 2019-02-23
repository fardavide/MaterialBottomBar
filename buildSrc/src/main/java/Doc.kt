import org.gradle.kotlin.dsl.KotlinBuildScript
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.dokka.gradle.DokkaTask

fun KotlinBuildScript.applyDokka() {
    apply( plugin = "org.jetbrains.dokka-android" )
    configure<DokkaTask> {
        outputFormat = "html"
    }
}