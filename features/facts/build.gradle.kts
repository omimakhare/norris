plugins {
    id("io.dotanuki.gradle.automodule")
}

dependencies {
    implementation(projects.platform.jvm.coreRest)
    implementation(projects.platform.android.coreAssets)
    implementation(projects.platform.android.coreNavigator)
    implementation(projects.platform.android.corePersistence)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.jvm)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.vm)
    implementation(libs.androidx.lifecycle.vm.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.swipetorefresh)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.google.material.design)

    testImplementation(projects.platform.android.testingPersistence)
    testImplementation(projects.platform.jvm.testingHelpers)
    testImplementation(projects.platform.jvm.testingRest)

    testImplementation(libs.junit4)
    testImplementation(libs.google.truth)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.testext.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.cashapp.turbine)

    androidTestImplementation(projects.platform.jvm.testingMockserver)
    androidTestImplementation(projects.platform.android.testingApplication)
    androidTestImplementation(projects.platform.android.testingHelpers)
    androidTestImplementation(projects.platform.android.testingPersistence)
    androidTestImplementation(projects.platform.android.testingScreenshots)

    androidTestImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.corektx)
    androidTestImplementation(libs.androidx.testext.junit)
    androidTestImplementation(libs.androidx.testext.junitktx)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.intents)
    debugImplementation(libs.square.leakcanary.core)
    androidTestImplementation(libs.square.leakcanary.instrumentation)

    androidTestImplementation(libs.adevinta.barista) {
        exclude(group = "org.jetbrains.kotlin")
        exclude(group = "org.checkerframework")
    }

    constraints {
        androidTestImplementation(libs.forced.jsoup)
    }
}
