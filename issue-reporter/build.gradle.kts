plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}

android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    defaultConfig {
        minSdk = 29
        targetSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions { jvmTarget = "1.8" }
}

dependencies {
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")

    // Github Eclipse
    implementation("org.eclipse.mylyn.github:org.eclipse.egit.github.core:2.1.5")

    // Sheets
    val sheets = "2.2.5"
    implementation("com.maxkeppeler.sheets:core:$sheets")
    implementation("com.maxkeppeler.sheets:input:$sheets")
    implementation("com.maxkeppeler.sheets:options:$sheets")
}

afterEvaluate {
    publishing {
        (publications) {
            val release by creating(MavenPublication::class) {
                from(components["release"])
            }
        }
    }
}
