#!/bin/sh
set -e
if command -v gradle >/dev/null 2>&1; then exec gradle "$@"; fi
DIST="${GRADLE_USER_HOME:-$HOME/.gradle}/wrapper/dists/gradle-8.11.1-bin"
ZIP="$DIST/gradle.zip"
DIR="$DIST/gradle-8.11.1"
if [ ! -x "$DIR/bin/gradle" ]; then mkdir -p "$DIST"; command -v curl >/dev/null 2>&1 && curl -L -o "$ZIP" https://services.gradle.org/distributions/gradle-8.11.1-bin.zip || wget -O "$ZIP" https://services.gradle.org/distributions/gradle-8.11.1-bin.zip; unzip -q -o "$ZIP" -d "$DIST"; fi
exec "$DIR/bin/gradle" "$@"
