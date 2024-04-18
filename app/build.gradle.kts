import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.tasks.Jar
import org.gradle.api.JavaVersion
import java.io.File
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import org.gradle.internal.impldep.org.eclipse.jgit.lib.ObjectChecker.encoding

plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.choosechef"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.choosechef"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.register<Javadoc>("javadoc") {
        // Configuración de la tarea Javadoc
        val mainSourceSet = sourceSets.getByName("main")
        source(mainSourceSet.java.srcDirs)

        classpath += files("C:/Users/helen/AppData/Local/Android/Sdk/sources/android-34/android.jar")
        classpath += configurations["implementation"]

        // Configuración de opciones de Javadoc
        val options = options as StandardJavadocDocletOptions
        options.addStringOption("Xdoclint:none", "-quiet")
    }
    tasks.register<Jar>("javadocJar") {
        dependsOn("javadoc")
        archiveClassifier.set("javadoc")
        from(tasks["javadoc"].outputs.files)
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.google.guava:guava:31.0.1-android")
//implementation("org.testng:testng:6.9.6")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:3.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation ("org.mockito:mockito-core:3.12.4")


//Retrofit & OkHttp for HTTP Calls implementation ("com.squareup.retrofit2:retrofit:2.3.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.3.0")
    implementation ("com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0")
    implementation ("com.squareup.okhttp3:okhttp:3.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:3.9.0")
}
