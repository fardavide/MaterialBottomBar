@file:Suppress("unused")

import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.plugins.MavenRepositoryHandlerConvention
import org.gradle.api.tasks.Upload
import org.gradle.kotlin.dsl.*


/**
 * @author Davide Giuseppe Farella.
 * A file containing params for publishing.
 */

fun KotlinBuildScript.publish( artifact: String ) = Project.let { p ->
    apply( plugin = "com.github.dcendents.android-maven" )
    apply( plugin = "com.jfrog.bintray" )

    val bintrayName = "${p.bintrayGroup}.$artifact"

    group = Project.groupId

    extra["bintrayRepo"] =          p.name
    extra["bintrayName"] =          bintrayName

    extra["libraryName"] =          artifact

    extra["publishedGroupId"] =     group
    extra["artifact"] =             artifact
    extra["libraryVersion"] =       p.versionName

    extra["libraryDescription"] =   p.description
    extra["siteUrl"] =              p.siteUrl
    extra["gitUrl"] =               p.gitUrl
    extra["developerId"] =          p.developerId
    extra["developerName"] =        p.developerName
    extra["developerEmail"] =       p.developerEmail
    extra["licenseName"] =          p.licenseName
    extra["licenseUrl"] =           p.licenseUrl
    extra["allLicenses"] =          p.allLicenses

    tasks.named<Upload>("install" ) {
        repositories {
            withConvention( MavenRepositoryHandlerConvention::class ) {
                mavenInstaller {
                    pom.project {
                        withGroovyBuilder {
                            "packaging"(    "aar" )
                            "groupId"(                  p.groupId )
                            "artifactId"(               artifact )
                            "name"(                     artifact )
                            "description"(              p.description )
                            "url"(                      p.siteUrl )
                            "licenses" {
                                "license" {
                                    "name"(             p.licenseName )
                                    "url"(      p.licenseUrl )
                                }
                            }
                            "developers" {
                                "developer" {
                                    "id"(               p.developerId )
                                    "name"(             p.developerName )
                                    "email"(    p.developerEmail )
                                }
                            }
                            "scm" {
                                "connection"(           p.gitUrl )
                                "developerConnection"(  p.gitUrl )
                                "url"(          p.siteUrl )
                            }
                        }
                    }
                }
            }
        }
    }

    configure<BintrayExtension> {
        user = typedProperty("bintray.user" )
        key = typedProperty("bintray.apikey" )

        pkg( delegateClosureOf<BintrayExtension.PackageConfig> {
            repo = extra.typedGet( "bintrayRepo" )
            name = extra.typedGet( "bintrayName" )
            desc = extra.typedGet( "libraryDescription" )
            websiteUrl = extra.typedGet( "siteUrl" )
            vcsUrl = extra.typedGet( "gitUrl" )
            setLicenses( * extra.typedGet( "allLicenses" ) )
            dryRun = false
            publish = true
            override = false
            publicDownloadNumbers = true

            version( delegateClosureOf<BintrayExtension.VersionConfig> {
                desc = extra.typedGet( "libraryDescription")
            } )
        } )
    }

    apply( from = "https://raw.githubusercontent.com/nuuneoi/JCenter/master/installv1.gradle" )
    apply( from = "https://raw.githubusercontent.com/nuuneoi/JCenter/master/bintrayv1.gradle" )
}

private inline fun <reified T> org.gradle.api.Project.typedProperty(s: String ) = project.findProperty( s ) as? T
private inline fun <reified T> ExtraPropertiesExtension.typedGet(name: String ) = get( name ) as? T