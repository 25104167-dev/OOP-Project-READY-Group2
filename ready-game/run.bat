@echo off
REM Compiles (if needed) and runs READY: Disaster Preparedness Adventure.
REM Usage: run.bat

cd /d "%~dp0"

where javac >nul 2>nul
if errorlevel 1 (
    echo ERROR: javac not found. Install a JDK 17+ and ensure it's on PATH ^(see README.md^).
    exit /b 1
)

if not exist out (
    mkdir out
)

dir /s /b src\main\java\*.java > sources.txt
javac -d out @sources.txt
if errorlevel 1 (
    echo Build failed.
    exit /b 1
)
del sources.txt

echo Starting READY...
java -cp out game.core.Game
