@echo off
setlocal enabledelayedexpansion

:: Check if NEW_VERSION is provided
if "%1"=="" (
    echo Error: Please provide NEW_VERSION as argument
    echo Usage: version-update.bat [NEW_VERSION]
    exit /b 1
)

set NEW_VERSION=%1

:: Update version.properties
for /f "tokens=*" %%i in ('type version.properties ^| find /v "PATCH_VERSION="') do (
    echo %%i >> version.properties.tmp
)
echo PATCH_VERSION=%NEW_VERSION% >> version.properties.tmp
move /y version.properties.tmp version.properties > nul

:: Git operations
git add version.properties
git commit -m "Bump version to %NEW_VERSION%"
git tag v%NEW_VERSION%

echo Version updated to %NEW_VERSION% and tagged
endlocal