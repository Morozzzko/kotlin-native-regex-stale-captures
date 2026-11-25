plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    macosArm64()
    js { nodejs() }
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs { nodejs() }

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
