plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    id("com.google.devtools.ksp")
}


android {
    namespace = "com.example.onefit"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.onefit"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val apiKey: String = project.findProperty("RAPID_API_KEY") as String? ?: ""

        buildConfigField("String", "RAPID_API_KEY", "\"$apiKey\"")

    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.21"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.compose.bom))
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")

    // Para desarrollo y debugging
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.4")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.4")

    // Opcional: Material Icons
    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    // Opcional: Integration con ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Opcional: Navigation con Compose
    implementation("androidx.navigation:navigation-compose:2.7.4")

    // Retrofit para HTTP
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Conversor JSON: Gson (puedes usar Moshi si prefieres)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Para coroutines (si no las tienes)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    implementation("io.coil-kt:coil-compose:2.2.2")

    // --- DEPENDENCIAS DE ROOM ---
    implementation(libs.room.runtime)
    implementation(libs.room.ktx) // Para Coroutines

    //Esta es la línea para KSP
    ksp(libs.room.compiler)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.hilt.navigation.compose)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.compose.runtime.livedata)
}