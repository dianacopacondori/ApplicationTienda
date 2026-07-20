plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.applicationtienda"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.applicationtienda"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
        testOptions {
            unitTests.isReturnDefaultValues = true

            // AGREGA ESTO PARA SILENCIAR LOS WARNINGS DE MOCKITO:
            unitTests.all {
                it.jvmArgs("-XX:+EnableDynamicAgentLoading")
            }
        }
}

dependencies {
    implementation(libs.androidx.monitor)
    implementation(libs.ext.junit)
    implementation(libs.engage.core)
    // Dependencias para pruebas unitarias (JUnit)
    testImplementation("junit:junit:4.13.2")
    // Dependencias básicas de Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Room Database para Java (annotationProcessor)
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    //Dependencias para pruebas de instrumentación (Android)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // JUnit 5 (Jupiter)
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Mockito
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")
}
//Activar JUnit5
tasks.withType<Test> {
    useJUnitPlatform()
}





