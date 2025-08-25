#!/bin/bash
# Update version.properties
sed -i "s/PATCH_VERSION=.*/PATCH_VERSION=$NEW_VERSION/" version.properties
git add version.properties
git commit -m "Bump version to $NEW_VERSION"
git tag v"$NEW_VERSION"