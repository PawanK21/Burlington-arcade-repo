#!/bin/bash
set -e

echo "Building Burlington Arcade with Maven..."
mvn clean package -DskipTests

echo "Build completed successfully!"
