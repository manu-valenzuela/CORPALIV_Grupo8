plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kover)
}

android {
    namespace = "cl.duoc.corpaliv"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "cl.duoc.corpaliv"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    // Kotest se ejecuta sobre JUnit 5
    testOptions {
        unitTests.all { it.useJUnitPlatform() }
    }
}

dependencies {
    // BOM de Compose gestiona las versiones unificadamente (debe ir primero)
    implementation(platform(libs.androidx.compose.bom))
    // Ubicación
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    // Librerías del proyecto vinculadas a la BOM
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // Navegación, ViewModel y WindowSizeClass
    implementation("androidx.navigation:navigation-compose:2.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    implementation("androidx.compose.material3:material3-window-size-class")


    // Carga de imágenes desde URL
    implementation(libs.coil.compose)

    // Consumo de API REST (API externa de indicadores)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // AndroidX Core y Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)

    // Tests y Debug
    // Pruebas unitarias de lógica: Kotest + MockK + corrutinas
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

    // Pruebas de UI: JUnit 4 + ComposeTestRule (se ejecutan en el emulador)
    androidTestImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
// Cobertura de la lógica (ViewModels, repositorios, modelos y utilidades).
// Se excluye lo visual (probado con ComposeTestRule) y lo que depende del hardware o de la red real.
kover {
    reports {
        filters {
            excludes {
                packages(
                    "cl.duoc.corpaliv.ui.screens",
                    "cl.duoc.corpaliv.ui.components",
                    "cl.duoc.corpaliv.ui.theme",
                    "cl.duoc.corpaliv.data.remote.dto"
                )
                classes(
                    "cl.duoc.corpaliv.MainActivity*",
                    "cl.duoc.corpaliv.ui.utils.NavegacionAplicacionKt*",
                    "cl.duoc.corpaliv.ui.utils.ManejadorPantallaAdaptativaKt*",
                    "cl.duoc.corpaliv.ui.utils.RutaAplicacion*",
                    "cl.duoc.corpaliv.ui.utils.ElementoNavegacion*",
                    "cl.duoc.corpaliv.data.ManejadorUbicacion*",
                    "cl.duoc.corpaliv.data.remote.RetrofitCorpaliv*",
                    "cl.duoc.corpaliv.data.remote.RetrofitIndicadores*",
                    "*ComposableSingletons*",
                    "*.BuildConfig"
                )
            }
        }
        verify {
            rule {
                minBound(80)
            }
        }
    }
}
