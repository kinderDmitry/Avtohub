@echo off
where gradle >nul 2>nul
if %ERRORLEVEL% EQU 0 ( gradle %* & exit /b %ERRORLEVEL% )
echo Gradle 8.11.1 is required. Install Gradle or run this project in Android Studio.
exit /b 1
