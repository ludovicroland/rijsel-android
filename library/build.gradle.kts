plugins {
  // Android
  alias(libs.plugins.android.library)

  // Kotlin
  alias(libs.plugins.kotlin.android)

  // maven
  `maven-publish`
}

android {
  namespace = "fr.rolandl.rijsel"

  compileSdk = Integer.parseInt(libs.versions.compile.sdk.get())

  defaultConfig {
    minSdk = Integer.parseInt(libs.versions.min.sdk.get())
    targetSdk = Integer.parseInt(libs.versions.compile.sdk.get())

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildFeatures {
    compose = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = "17"
  }

  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
    }

    debug {
      isMinifyEnabled = false
    }
  }

}

dependencies {
  // Kotlin
  api(libs.android.core.ktx)

  // Android
  api(libs.android.fragment.ktx)

  // Compose
  api(platform(libs.compose.bom))
  api(libs.compose.material)
  api(libs.compose.ui.tooling)
  api(libs.compose.activity)
  api(libs.compose.constraintlayout)
  api(libs.compose.lifecycle.viewmodel)
  api(libs.compose.lifecycle.runtime)

  // Third party
  api(libs.timber)
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      groupId = "fr.rolandl"
      artifactId = "rijsel"
      version = "2.0.0-beta1"

      afterEvaluate {
        from(components["release"])
      }
    }
  }
}