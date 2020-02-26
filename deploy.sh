#!/bin/bash

set -e -u

export DEPLOY_SOURCE_DIR="site/build/libs"
export SKIP_DEPENDENCY_LIST="true"

git clone https://github.com/perfectsense/travis-s3-deploy.git
travis-s3-deploy/deploy.sh
