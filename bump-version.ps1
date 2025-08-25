@echo off
set NEW_VERSION=%1
powershell -Command "(Get-Content version.properties) -replace 'PATCH_VERSION=.*', 'PATCH_VERSION=%NEW_VERSION%' | Set-Content version.properties"
git add version.properties
git commit -m "Bump version to %NEW_VERSION%"
git tag v%NEW_VERSION%