#!/bin/bash

# Design Patterns GUI Showcase Launcher
# This script compiles and runs the interactive GUI demonstration

echo "🎯 Design Patterns Interactive Showcase"
echo "========================================"

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed or not in PATH"
    echo "Please install Java 8 or higher to run this application"
    exit 1
fi

# Check if javac is installed
if ! command -v javac &> /dev/null; then
    echo "❌ Java compiler (javac) is not installed or not in PATH"
    echo "Please install JDK to compile and run this application"
    exit 1
fi

echo "☕ Java version:"
java -version

echo ""
echo "🔨 Compiling Java files..."

# Create bin directory if it doesn't exist
mkdir -p bin

# Compile all Java files
find src -name "*.java" -print0 | xargs -0 javac -d bin -cp src

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo ""
    echo "🚀 Starting GUI application..."
    echo "   Close the application window to exit"
    echo ""

    # Run the GUI application
    java -cp bin gui.DesignPatternShowcase

    echo ""
    echo "👋 Thanks for exploring Design Patterns!"
else
    echo "❌ Compilation failed!"
    echo "Please check the error messages above and fix any issues"
    exit 1
fi
