#! /usr/bin/env bash

set -e

readonly workdir="$GITHUB_WORKSPACE"
readonly properties_file="$workdir/gradle.properties"
readonly gha_jdk_path="$1"

write_property() {
    echo "$1" >>$properties_file
}

write_common_properties() {
    echo "🔥 Writting common Gradle properties regarding the GHA runner"

    # Gradle properties common to all build environments
    write_property "org.gradle.parallel=true"
    write_property "org.gradle.configureondemand=false"
    write_property "org.gradle.caching=true"
    write_property "org.gradle.daemon=false"
    write_property "org.gradle.logging.stacktrace=all"

    # Kotlin properties common to all build environments
    write_property "kotlin.code.style=official"
    write_property "kotlin.incremental=false"

    # Android properties common to all build environments
    write_property "android.nonTransitiveRClass=true"
    write_property "android.useAndroidX=true"
    write_property "android.defaults.buildfeatures.viewbinding=true"
    write_property "android.defaults.buildfeatures.resvalues=true"
    write_property "android.defaults.buildfeatures.aidl=false"
    write_property "android.defaults.buildfeatures.renderscript=false"
    write_property "android.defaults.buildfeatures.shaders=false"
    write_property "android.defaults.buildfeatures.buildconfig=true"

    # JDK path exposed by actions/setup-java
    write_property "org.gradle.java.installations.paths=$gha_jdk_path"
}

write_macos_properties() {
    echo "🔥 Fine tunning Gradle properties for MacOS GHA runner"
    write_property "org.gradle.jvmargs=-Xmx7g -XX:+UseParallelGC -Dfile.encoding=UTF-8"
    write_property "kotlin.daemon.jvmargs=-Xmx3g -Xms512m -XX:+UseParallelGC -Dfile.encoding=UTF-8"
    write_property "org.gradle.parallel.threads=3"
}

write_linux_properties() {
    echo "🔥 Fine tunning Gradle properties for Linux GHA runner"
    write_property "org.gradle.jvmargs=-Xmx4g -Xms512m -XX:+UseParallelGC -Dfile.encoding=UTF-8"
    write_property "kotlin.daemon.jvmargs=-Xmx2g -Xms512m -XX:+UseParallelGC -Dfile.encoding=UTF-8"
    write_property "org.gradle.parallel.threads=2"
}

rm "$properties_file" && touch "$properties_file"

echo
write_common_properties

case "$RUNNER_OS" in
"macOS")
    write_macos_properties
    ;;
*)
    write_linux_properties
    ;;
esac

echo
cat $properties_file
echo
