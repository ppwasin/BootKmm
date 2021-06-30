# Spec
- XcodeGen + Cocoapod

# Spike
## XcodeGen + SPM
### Result
Currently not that good since it run more steps compare to cocoapods approach. 
- ` ./gradlew createSwiftPackage`
- xcode build project again

```diff
+ Note that: may return to use this if we can automate these steps to run whenever shared code changed. 
```

### Setup
shared/build.gradle.kts
```kotlin
plugins {
  ...
  id("com.chromaticnoise.multiplatform-swiftpackage") version "2.0.3"
  ...
}
/** Must be consistent: shared, swiftpackage
 * ## ../iosApp/project.yml ###
 * packages:
 *  shared:
 *   path: ../shared/swiftpackage
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
```

iosApp/project.yml
```yml
name: iosAppXcodeGen
packages:
  shared:
    path: ../shared/swiftpackage
options:
  bundleIdPrefix: com.boot.iosApp
  deploymentTarget:
    iOS: 14.0
targets:
  iosApp:
    type: application
    platform: iOS
    sources:
      - path: iosApp
    dependencies:
      - package: shared
```

# Generate xcodegen project
- cd iosApp
- xcodegen && pod install && xed iosApp.xcworkspace

# Workflow
## When update shared source
- On XCode build project again