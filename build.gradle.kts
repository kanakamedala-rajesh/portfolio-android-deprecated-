buildscript {
    repositories {
        google()
        jcenter()

    }
    dependencies {
        classpath(PluginClassPaths.PLUGIN_CLASSPATH_ANDROID_GRADLE)
        classpath(PluginClassPaths.PLUGIN_CLASSPATH_KOTLIN_GRADLE)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()

    }
}

tasks.register("clean").configure {
    delete("build")
}