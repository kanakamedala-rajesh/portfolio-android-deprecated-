plugins {
    id(Plugins.PLUGINS_ANDROID_APPLICATION)
    id(Plugins.PLUGINS_KOTLIN_ANDROID)
    id(Plugins.PLUGINS_HILT)
    id(Plugins.KOTLIN_KAPT)
}

android {
    compileSdkVersion(AndroidSdk.COMPILE_SDK_VERSION)
    buildToolsVersion(AndroidSdk.BUILD_TOOLS_VERSION)

    defaultConfig {
        applicationId = "com.venkatasudha.portfolio"
        minSdkVersion(AndroidSdk.MIN_SDK_VERSION)
        targetSdkVersion(AndroidSdk.TARGET_SDK_VERSION)
        versionCode = PORTFOLIO_VERSION_CODE
        versionName = PORTFOLIO_VERSION_NAME

        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("signing-config") {
            storeFile = file("../apk-signing/release-keystore")
            storePassword = System.getenv("STORE_PASSWORD") ?: "android"
            keyAlias = "android"
            keyPassword = System.getenv("KEY_PASSWORD") ?: "android"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("signing-config")
        }
        getByName("debug") {
            versionNameSuffix = "-debug"
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    // AndroidX Dependencies
    implementation(AndroidxLibraries.CORE_KTX)
    implementation(AndroidxLibraries.APP_COMPAT)
    implementation(AndroidxLibraries.CONSTRAINT_LAYOUT)
    implementation(AndroidxLibraries.ACTIVITY_KTX)

    // External Dependencies
    implementation(ExternalLibraries.KOTLIN)
    implementation(ExternalLibraries.MATERIAL_DESIGN)
    implementation(ExternalLibraries.TIMBER)
    implementation(ExternalLibraries.HILT_ANDROID)

    // Annotation Processor Dependencies
    kapt(ExternalLibraries.HILT_ANDROID_COMPILER)

    // Unit Test Dependencies
    implementation(TestLibraries.JUNIT)

    // UI Test Dependencies Dependencies
    androidTestImplementation(AndroidTestLibraries.JUNIT_TEST_EXT)
    androidTestImplementation(AndroidTestLibraries.ESPRESSO_CORE)
}