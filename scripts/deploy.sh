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

# Find the generated JAR (excluding original-*)
JAR_FILE=$(find target -maxdepth 1 -name "*.jar" ! -name "original-*" -type f | head -1)

if [ -z "$JAR_FILE" ] || [ ! -f "$JAR_FILE" ]; then
    echo "❌ JAR file not found in target!"
    exit 1
fi

JAR_NAME=$(basename "$JAR_FILE")

echo "✅ Build successful!"
echo "📂 JAR deployed to: $PLUGINS_DIR/$JAR_NAME"
echo "✨ Done!"
