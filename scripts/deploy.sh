#!/bin/bash

# Configuration
PLUGINS_DIR="/home/xpg/Downloads/paper-server-teste/plugins"
PROJECT_DIR="$(pwd)"

echo "🚀 Starting Deployment..."

# 0. Check if server is running (Port 25565)
echo "🔍 Checking for running server..."
if ss -tuln | grep -q ":25565 "; then
    echo "⚠️  WARNING: A server appears to be running on port 25565!"
    echo "Deployment while the server is running might fail due to file locking."
    echo "Please STOP the server and run this script again."
    echo "------------------------------------------------"
    sleep 3
fi

# 1. Build the project
echo "📦 Building project with Maven..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

# The pom.xml already handles the copy via maven-antrun-plugin,
# but we can verify it here.
JAR_FILE="target/plugin-template.jar"

if [ ! -f "$JAR_FILE" ]; then
    echo "❌ JAR file not found in target!"
    exit 1
fi

echo "✅ Build successful!"
echo "📂 JAR deployed to: $PLUGINS_DIR"
echo "✨ Done!"
