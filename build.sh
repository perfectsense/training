#!/bin/bash

set -e

if [[ $(git rev-parse --is-shallow-repository) == "true" ]]
then
    git fetch --unshallow
fi

if [ ! -z $TRAVIS_COMMIT_RANGE ]; then
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
        version="PR$TRAVIS_PULL_REQUEST"

    elif [[ "$TRAVIS_TAG" =~ ^v[0-9]+\. ]]; then
        version=${TRAVIS_TAG/v/}

    else
        COMMIT_COUNT=$(git rev-list --count HEAD)
        COMMIT_SHA=$(git rev-parse --short HEAD)

        version=$(git describe --tags --match "v[0-9]*" HEAD)
        version=${version/v/}
        version=${version:-0.$COMMIT_COUNT-$COMMIT_SHA}
        version+=+$TRAVIS_BUILD_NUMBER

    fi

    echo "Building version ${version}"

    if [[ "$DISABLE_BUILD_SCAN" == "true" ]]; then
        ./gradlew -Prelease="${version}"
     else
        ./gradlew -Prelease="${version}" --scan
     fi
fi
