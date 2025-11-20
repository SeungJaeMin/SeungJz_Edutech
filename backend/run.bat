@echo off
echo.
echo ====================================
echo   SeungJz Edutech Backend Server
echo ====================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

echo ✅ Java found
java -version
echo.

REM Create uploads directory
if not exist "uploads\videos" mkdir uploads\videos
if not exist "uploads\thumbnails" mkdir uploads\thumbnails
echo ✅ Upload directories created
echo.

REM Check for JAR file
set JAR_FILE=build\libs\backend-0.0.1-SNAPSHOT.jar

if exist "%JAR_FILE%" (
    echo ✅ JAR file found: %JAR_FILE%
    echo.
    echo 🚀 Starting backend server...
    echo.
    java -jar "%JAR_FILE%"
) else (
    echo ❌ JAR file not found: %JAR_FILE%
    echo.
    echo Please build the project first using one of these methods:
    echo   1. IntelliJ IDEA: Run 'Gradle -> Tasks -> build -> build'
    echo   2. Eclipse: Run 'Gradle build'
    echo   3. Command line: gradlew build
    echo.
    pause
)
