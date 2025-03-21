//plugins {
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.google.gms.google.services)
//}
//
//android {
//    namespace = "com.noureddine.WriteFlow"
//    compileSdk = 35
//
//    defaultConfig {
//        applicationId = "com.noureddine.WriteFlow"
//        minSdk = 28
//        targetSdk = 35
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//}
//
//dependencies {
//
//    // Retrofit & JSON parsing
//    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
//    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
//    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")
//
//    // ViewModel and LiveData
//    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
//    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
//    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
//    implementation ("androidx.activity:activity-ktx:1.8.0")
//
//    implementation ("org.apache.poi:poi:5.2.3")
//    implementation ("org.apache.poi:poi-ooxml:5.2.3")
//
//    implementation ("com.itextpdf:itextpdf:5.5.13.3")
//
//    implementation ("androidx.security:security-crypto:1.1.0-alpha06")
//
//    implementation("com.google.firebase:firebase-auth:22.3.1")
//    implementation("com.google.android.gms:play-services-auth:21.1.0")
//
//
//    implementation(libs.appcompat)
//    implementation(libs.material)
//    implementation(libs.activity)
//    implementation(libs.constraintlayout)
//    implementation(libs.firebase.auth)
//    implementation(libs.credentials)
//    implementation(libs.credentials.play.services.auth)
//    implementation(libs.googleid)
//    implementation(libs.firebase.database)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.ext.junit)
//    androidTestImplementation(libs.espresso.core)
//}




plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.noureddine.WriteFlow"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.noureddine.WriteFlow"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Room schema export directory - fixed syntax for Kotlin DSL
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
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
}

dependencies {
    // Room Database
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    // Optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:$roomVersion")
    // Optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")

    // Retrofit & JSON parsing
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    implementation ("com.unity3d.ads:unity-ads:4.9.2")

    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    implementation("com.itextpdf:itextpdf:5.5.13.3")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.android.gms:play-services-auth:21.1.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

