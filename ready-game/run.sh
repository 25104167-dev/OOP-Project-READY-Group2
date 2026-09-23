#!/usr/bin/env bash
# Compiles (if needed) and runs READY: Disaster Preparedness Adventure.
# Usage: ./run.sh
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

if ! command -v javac >/dev/null 2>&1; then
    echo "ERROR: javac not found. Install a JDK 17+ (see README.md)." >&2
    exit 1
fi

if [ ! -d out ] || [ -z "$(find out -name '*.class' 2>/dev/null)" ]; then
    echo "Compiling..."
    mkdir -p out
    find src/main/java -name "*.java" > /tmp/ready_sources.txt
    javac -d out @/tmp/ready_sources.txt
fi

echo "Starting READY..."
java -cp out game.core.Game
