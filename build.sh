#!/bin/bash

set -e

if [ ! -z $TRAVIS_COMMIT_RANGE ] ; then
    if ! git diff --name-only $TRAVIS_COMMIT_RANGE | grep -qvE '(^ops|^docker)' ; then
        echo "Commit range $TRAVIS_COMMIT_RANGE does not contain buildable project code; not running the CI build."
        exit
    fi
fi

if [ -z "$TRAVIS_BUILD_NUMBER" ]; then
  if [[ "$DISABLE_BUILD_SCAN" == "true" ]]; then
      ./gradlew
   else
      ./gradlew --scan
   fi
else

    version=""
    if [[ "$TRAVIS_PULL_REQUEST" != "false" ]]; then
        version="PR-$TRAVIS_PULL_REQUEST"
    else
        if [[ "$TRAVIS_TAG" =~ ^v[0-9]+\. ]]; then
            version=${TRAVIS_TAG/v/}

        else
            IS_SHALLOW_REPOSITORY=$(git rev-parse --is-shallow-repository)
            if [[ "$IS_SHALLOW_REPOSITORY" == "true" ]]
            then
                git fetch --unshallow
            fi

            COMMIT_COUNT=$(git rev-list --count HEAD)
            COMMIT_SHA=$(git rev-parse --short HEAD)
            version=$COMMIT_COUNT-$COMMIT_SHA+$TRAVIS_BUILD_NUMBER
        fi
    fi

    echo "Building version ${version}"

    if [[ "$DISABLE_BUILD_SCAN" == "true" ]]; then
        ./gradlew -Prelease="${version}"
     else
        ./gradlew -Prelease="${version}" --scan
     fi
fi
