#!/bin/zsh

# Local release script for testing the build process
# Usage: ./scripts/local-release.sh

# Exit on error
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
VERSION=$(grep "^app.version=" $SCRIPT_DIR/../src/main/resources/app.properties | cut -d'=' -f2)
echo "Building Tetraj release $VERSION"

# Auto-format all Java files using Google Java Format via Spotless
echo "Running spotlessApply to format code..."
./gradlew spotlessApply --no-daemon

# Build shadowJar
echo "Building shadow JAR..."
./gradlew shadowJar --no-daemon

# Copy JAR with version
echo "Packaging JAR..."
rm -fR tetraj-*.jar
cp build/libs/tetraj-all.jar tetraj.jar

# Generate Javadoc
echo "Generating Javadoc..."
./gradlew javadoc --no-daemon
