import com.google.protobuf.gradle.id
import java.util.Properties
import java.io.FileInputStream

// Kotlin DSL

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.0"
    id ("com.google.relay") version "0.3.09"
    id ("com.google.protobuf") version "0.9.4"
}

android {
    namespace = "com.soldevcode.composechat"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.soldevcode.composechat"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        // Access API key stored in local.properties Gradle 8.0 and above
        val properties = Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            properties.load(FileInputStream(localPropertiesFile))
        }
        val apiKey = properties.getProperty("CHATGPT_API_KEY") ?: ""

        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                "String",
                "GPT_KEY",
                apiKey,
            )
        }
        debug {
            buildConfigField(
                "String",
                "GPT_KEY",
                apiKey,
            )
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/license.txt"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/notice.txt"
            excludes += "META-INF/ASL2.0"
            excludes += "META-INF/INDEX.LIST"
        }
    }
}

dependencies {

    implementation("androidx.compose.material3:material3-android:1.2.0-beta02")
    //koin
    val koin_version = "3.5.0"
    implementation("io.insert-koin:koin-android:$koin_version")
    implementation ("io.insert-koin:koin-androidx-compose:$koin_version")
    implementation ("io.insert-koin:koin-androidx-navigation:$koin_version")


    implementation("androidx.navigation:navigation-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0-beta01")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-core:2.6.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.4.3")

    //delete this
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation ("com.squareup.retrofit2:converter-scalars:2.1.0")

    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.9.0")

    /// Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")

    implementation ("io.grpc:grpc-okhttp:1.38.1")
    implementation ("io.grpc:grpc-stub:1.38.1")
    implementation ("com.google.auth:google-auth-library-oauth2-http:0.26.0")
    implementation ("com.google.api-client:google-api-client-android:1.31.5")
    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("com.google.cloud:google-cloud-speech:1.29.1")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui-graphics-android:1.5.0")
    implementation("com.google.android.gms:play-services-mlkit-text-recognition-common:19.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Proto data store without java-lite
    implementation  ("androidx.datastore:datastore:1.0.0")
    implementation("com.google.protobuf:protobuf-java:3.17.3")
    //implementation ("com.google.protobuf:protobuf-lite:3.0.0")
    //implementation ("com.google.protobuf:protobuf-javalite:3.25.2")
    /*implementation ("com.google.protobuf:protobuf-javalite:3.25.2")*//*{
        exclude( group = "com.google.protobuf", module = "protobuf-java:3.17.2")
    }*/


}
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.17.3" // Specify the version of protoc you want to use
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    // Optionally, configure options for the java generation here
                }
            }
        }
    }
}



