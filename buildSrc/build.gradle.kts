plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(gradleApi())
}
allprojects {
    repositories {
        mavenCentral()
    }
}

gradlePlugin {
    plugins {
        create("find_untranslated_plugin") {
            id = "find_untranslated_plugin"
            implementationClass = "com.skrainyukov.buildsrc.FindUntranslatedStringsPlugin"
        }
    }
}