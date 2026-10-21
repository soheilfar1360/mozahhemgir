plugins {
    id("com.android.application")
}

android {
    namespace = "art.navastudio.mozahhemgir"
    compileSdk = 36

    defaultConfig {
        applicationId = "art.navastudio.mozahhemgir"
        minSdk = 29
        targetSdk = 36
        versionCode = 3
        versionName = "0.3.0"

        testInstrumentationRunner = "android.test.InstrumentationTestRunner"
    }

    sourceSets.getByName("main").assets.srcDir(rootProject.file("rules"))

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}
