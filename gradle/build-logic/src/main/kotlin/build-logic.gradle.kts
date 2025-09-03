@file:Suppress("UnstableApiUsage")

import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.MavenPublishBaseExtension

subprojects {
    plugins.withId("com.vanniktech.maven.publish.base") {
        configure<MavenPublishBaseExtension> {
            group = "icu.ketal.katexmath"
            version = "0.0.1-alpha09"
            pomFromGradleProperties()
            publishToMavenCentral()
            signAllPublications()
            configure(
                AndroidSingleVariantLibrary(
                    variant = "release",
                    sourcesJar = true,
                    publishJavadocJar = false
                )
            )
        }
    }
}
