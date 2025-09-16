@echo off
REM Design Patterns GUI Showcase Launcher for Windows
REM This script compiles and runs the interactive GUI demonstration

echo 🎯 Design Patterns Interactive Showcase
echo ========================================

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed or not in PATH
    echo Please install Java 8 or higher to run this application
    pause
    exit /b 1
)

REM Check if javac is installed
javac -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java compiler (javac) is not installed or not in PATH
    echo Please install JDK to compile and run this application
    pause
    exit /b 1
)

echo ☕ Java version:
java -version

echo.
echo 🔨 Compiling Java files...

REM Create bin directory if it doesn't exist
if not exist bin mkdir bin

REM Compile all Java files
for /r src %%f in (*.java) do (
    javac -d bin -cp src "%%f"
)

if %errorlevel% equ 0 (
    echo ✅ Compilation successful!
    echo.
    echo 🚀 Starting GUI application...
    echo    Close the application window to exit
    echo.

    REM Run the GUI application
    java -cp bin gui.DesignPatternShowcase

    echo.
    echo 👋 Thanks for exploring Design Patterns!
) else (
    echo ❌ Compilation failed!
    echo Please check the error messages above and fix any issues
    pause
    exit /b 1
)

pause
