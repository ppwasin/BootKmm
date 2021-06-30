import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("com.chromaticnoise.multiplatform-swiftpackage") version "2.0.3"
}

version = "1.0"

kotlin {
    android()


    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iosTarget("ios") { }

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.0"
        frameworkName = "shared"
        podfile = project.file("../iosApp/Podfile")
    }
    
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
        val iosMain by getting
        val iosTest by getting
    }
    
//    targets.withType<KotlinNativeTarget> {
//        binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework> {
//            isStatic = false
//        }
//    }
}

android {
    compileSdk = 30
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 23
        targetSdk = 30
    }
}

/** Must be consistent: shared, swiftpackage
 * ## ../iosApp/project.yml ###
 * packages:
 *  shared:                          >>> packageName("shared")
 *   path: ../shared/swiftpackage    >>> outputDirectory(File(projectDir, "swiftpackage"))
 * */

/** When change common file must run:
 * - ./gradlew createSwiftPackage
 * - xcode build project again
* */
multiplatformSwiftPackage {
    packageName("shared")
    outputDirectory(File(projectDir, "swiftpackage"))
    
    swiftToolsVersion("5.3")
    targetPlatforms {
        iOS { v("14") }
    }
    distributionMode { local() }
}