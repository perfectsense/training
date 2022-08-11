#!/bin/sh

if [[ -z $1 ]]
then
    echo "USAGE: $0 [ build | serve | bundle-update ]"
    exit 1
fi

set -eu
export JEKYLL_VERSION=3.8.6

case $1 in
    build)
        docker run --rm \
        --volume="$PWD:/srv/jekyll:cached" \
        --volume="$PWD/.bundle:/usr/local/bundle:cached" \
        -it jekyll/jekyll:$JEKYLL_VERSION \
        jekyll build
    ;;
    serve)
        docker run --rm \
        --volume="$PWD:/srv/jekyll:cached" \
        --volume="$PWD/.bundle:/usr/local/bundle:cached" \
        --publish 4000:4000 \
        jekyll/jekyll:$JEKYLL_VERSION \
        jekyll serve --watch
    ;;
    bundle-update)
        docker run --rm \
        --volume="$PWD:/srv/jekyll:cached" \
        --volume="$PWD/.bundle:/usr/local/bundle:cached" \
        -it jekyll/jekyll:$JEKYLL_VERSION \
        bundle update
    ;;
    *)
        docker run --rm \
        --volume="$PWD:/srv/jekyll:Z" \
        --volume="$PWD/.bundle:/usr/local/bundle:cached" \
        -it jekyll/jekyll:$JEKYLL_VERSION \
        $*
    ;;
esac
