plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    macosArm64()

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
