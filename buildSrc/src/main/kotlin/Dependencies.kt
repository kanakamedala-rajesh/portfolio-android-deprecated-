import java.io.File
import java.util.concurrent.TimeUnit

const val KOTLIN_VERSION = "1.4.21"

object PortfolioVersions {
    var VERSION_NAME_BUILD_NUMBER = generateVersionCode()

    const val VERSION_NAME_MAJOR = 0
    const val VERSION_NAME_MINOR = 0
    const val VERSION_NAME_PATCH = 1
}

var PORTFOLIO_VERSION_CODE = PortfolioVersions.VERSION_NAME_MAJOR * 10000 + PortfolioVersions.VERSION_NAME_MINOR * 1000 + PortfolioVersions.VERSION_NAME_PATCH * 100 + PortfolioVersions.VERSION_NAME_BUILD_NUMBER
var PORTFOLIO_VERSION_NAME = "${PortfolioVersions.VERSION_NAME_MAJOR}.${PortfolioVersions.VERSION_NAME_MINOR}.${PortfolioVersions.VERSION_NAME_PATCH}.${PortfolioVersions.VERSION_NAME_BUILD_NUMBER}"

object PluginClassPaths {
    object Versions {
        const val GRADLE_BUILD_VERSION = "4.0.0"
    }

    const val PLUGIN_CLASSPATH_ANDROID_GRADLE = "com.android.tools.build:gradle:${Versions.GRADLE_BUILD_VERSION}"
    const val PLUGIN_CLASSPATH_KOTLIN_GRADLE = "org.jetbrains.kotlin:kotlin-gradle-plugin:$KOTLIN_VERSION"
}

object Plugins {
    const val PLUGINS_ANDROID_APPLICATION = "com.android.application"
    const val PLUGINS_KOTLIN_ANDROID = "kotlin-android"
}

object AndroidSdk {
    const val MIN_SDK_VERSION = 21
    const val COMPILE_SDK_VERSION = 30
    const val TARGET_SDK_VERSION = 30
    const val BUILD_TOOLS_VERSION = "30.0.3"
}

object AndroidxLibraries {
    private object Versions {
        //androidx dependency version
        const val APP_COMPAT_VERSION = "1.1.0"
        const val CORE_KTX_VERSION = "1.2.0"
        const val CONSTRAINT_LAYOUT_VERSION = "1.1.3"
    }

    const val APP_COMPAT = "androidx.appcompat:appcompat:${Versions.APP_COMPAT_VERSION}"
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX_VERSION}"
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT_VERSION}"
}

object ExternalLibraries {
    object Versions {
        // external dependency versions
        const val MATERIAL_DESIGN_VERSION = "1.2.0"
    }

    const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$KOTLIN_VERSION"
    const val MATERIAL_DESIGN = "com.google.android.material:material:${Versions.MATERIAL_DESIGN_VERSION}"
}

object TestLibraries {
    private object Versions {
        // test dependency versions
        const val JUNIT_VERSION = "4.13"
    }

    const val JUNIT = "junit:junit:${Versions.JUNIT_VERSION}"
}

object AndroidTestLibraries {
    private object Versions {
        // android test dependency version
        const val ESPRESSO_CORE_VERSION = "3.2.0"
        const val JNUIT_TEST_EXT_VERSION = "1.1.1"
    }

    const val JNUIT_TEST_EXT = "androidx.test.ext:junit:${Versions.JNUIT_TEST_EXT_VERSION}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE_VERSION}"
}

fun generateVersionCode(): Int {
    var result = "git rev-list HEAD --count".runCommand().trim()
    if (result.isEmpty()) result =
        "PowerShell -Command git rev-list HEAD --count".runCommand().trim()
    if (result.isEmpty()) throw RuntimeException("Could not generate versioncode on this platform? Cmd output: $result")
    return result.toInt()
}

fun String.runCommand(workingDir: File = File("./")): String {
    val parts = this.split("\\s".toRegex())
    val proc = ProcessBuilder(*parts.toTypedArray())
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()

    proc.waitFor(1, TimeUnit.MINUTES)
    return proc.inputStream.bufferedReader().readText().trim()
}