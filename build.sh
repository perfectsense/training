#!/bin/bash

set -e -u

if [ -z "$TRAVIS_BUILD_NUMBER" ]; then
    ./gradlew
else

    version=""
    if [[ "$TRAVIS_PULL_REQUEST" != "false" ]]; then
        version="1.0-PR$TRAVIS_PULL_REQUEST"
    else
        version="1.0.0"-`date +%Y%m%d.%H%M%S`-$TRAVIS_BUILD_NUMBER
    fi

    echo "Building version ${version}"

    ./gradlew -Prelease="${version}"
fi
