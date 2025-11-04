/*
 * Copyright (c) 2015-2020 Uli Bubenheimer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode.NO_COMPATIBILITY
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.Companion.fromTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

group = "org.bubenheimer"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(libs.versions.java.toolchain.get().toInt())

    explicitApi()

    compilerOptions {
        jvmTarget = fromTarget(libs.versions.java.source.get())
        progressiveMode = true
        jvmDefault = NO_COMPATIBILITY
        verbose = false
        extraWarnings = true
        freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")

        // DelicateCoroutinesApi is counterproductive
        optIn.add("kotlinx.coroutines.DelicateCoroutinesApi")
        // Also counterproductive:
        optIn.add("kotlinx.coroutines.ObsoleteCoroutinesApi")
    }
}

android {
    namespace = "org.bubenheimer.android.util"

    libs.versions.android.sdk.compile.get().let {
        it.toIntOrNull()?.let { compileSdk = it } ?: run { compileSdkPreview = it }
    }

    defaultConfig {
        minSdk = libs.versions.android.sdk.min.get().toInt()
        consumerProguardFiles("proguard-rules.pro")
    }

    buildTypes {
        getByName("debug") {
        }

        getByName("release") {
            isShrinkResources = false
            isMinifyEnabled = false
            //TODO
//            isDebuggable = false
            isJniDebuggable = false
        }
    }

    compileOptions {
        JavaVersion.toVersion(libs.versions.java.source.get()).let {
            sourceCompatibility = it
            targetCompatibility = it
        }
    }

    buildFeatures {
        buildConfig = false
    }

    publishing {
        multipleVariants {
            allVariants()
            withSourcesJar()
        }
    }

    lint {
        quiet = false
        abortOnError = false
        checkReleaseBuilds = true
        ignoreWarnings = false
        absolutePaths = true
        checkAllWarnings = true
        warningsAsErrors = true
        // if true, don't include source code lines in the error output
        noLines = false
        // if true, show all locations for an error, do not truncate lists, etc.
        showAll = true
        // whether lint should include full issue explanations in the text error output
        explainIssues = true
        textReport = false
        xmlReport = false
        htmlReport = true
        // optional path to HTML report (default will be lint-results.html in the builddir)
        //htmlOutput file("$reportsDir/lint-report.html")
        checkTestSources = true
        ignoreTestSources = false
        checkGeneratedSources = false
        checkDependencies = false
    }

    dependencies {
        implementation(libs.bubenheimer.kotlinutil)

        implementation(libs.androidx.annotation)
        implementation(libs.androidx.core.ktx)

        testImplementation(libs.junit)

        testImplementation(libs.kotlin.test.junit)
    }
}

publishing {
    publications {
        create<MavenPublication>(name = "library") {
            afterEvaluate {
                from(components["default"])
            }
        }
    }
}
