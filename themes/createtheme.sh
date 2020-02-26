#!/bin/sh

set -e -u

name=""
theme=brightspot-theme-dk
version=""

for ARGUMENT in "$@"
do

    KEY=$(echo $ARGUMENT | cut -f1 -d=)
    VALUE=$(echo $ARGUMENT | cut -f2 -d=)

    case "$KEY" in
            theme)    theme=${VALUE} ;;
            version)    version=${VALUE} ;;
            name)    name=${VALUE} ;;
            *)
    esac
done

if [[ -z $name  ]]
then
    echo ""
    echo "Name required"
    echo "Example createtheme.sh name=projet-theme-test"
    echo ""
    echo "Optional arguments theme=brightspot-theme-dk version=4.1.5"
    echo "theme (defaults to brightspot-theme-dk)"
    echo "version (defaults to latest)"
    echo "Example createtheme.sh name=projet-theme-test theme=brightspot-theme-dk version=4.1.5"
    echo ""
    exit 0;
fi

if [[ -z $version  ]]
then
    versionsHtml=$(curl --silent https://artifactory.psdops.com/npm-releases/$theme/-/)

    for string in "$(echo $versionsHtml | grep -o "<a href=\"${theme}-v[^\"]*")"
    do
        match="${match:+$match }$string"
        match="${match//<a href=\"${theme}-v}"
        match="${match//.tgz}"

        IFS=$'\n'; set -f; versions=($match)

        function version_gt() { test "$(printf '%s\n' "$@" | sort -V | head -n 1)" != "$1"; }
        version=0

        tagKey="refs/tags/v"
        for (( i=0; i<${#versions[@]}; i++ ))
        do
            tagVersion=${versions[$i]}
            tagVersion=${tagVersion##*refs/tags/v}
            versions[$i]=$tagVersion
            if [[ ${tagVersion} != *"-rc"* ]];then

                if [[ ${tagVersion} == "4.1"* ]];then

                  blacklisted=0

                  #Blacklist
                  if [[  $tagVersion == "4.1.4179-x87d539" || $tagVersion == "4.1.4280-xf73eb0" || $tagVersion == "4.1.4335-x3cb1a3" || $tagVersion == "4.1.4347-x9c9c36" ]]; then
                      blacklisted=1
                  fi

                  if [[ ${blacklisted} == 0 ]]; then
                    if version_gt $tagVersion $version; then
                         version=$tagVersion
                    fi
                  fi
                fi
            fi
        done
    done
fi

if [[ ${version} == 0 ]]; then
  echo "Unable to automatically pick the latest theme version, please provide."
  exit 0;
fi


echo ""
echo "Building Theme '${name}' from ${theme} version ${version}"

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

echo "Directory $DIR"

mkdir -p $DIR/$name
echo "Download theme https://artifactory.psdops.com/npm-releases/$theme/-/$theme-v${version}.tgz"
curl -SL https://artifactory.psdops.com/npm-releases/$theme/-/$theme-v${version}.tgz | tar -xf - -C $DIR/$name/ --strip-components=1

sed -i "" "s/${theme}/${name}/g" ${DIR}/${name}/package.json

echo "# DO NOT REMOVE - Required for JarBundleInitializer to find theme" > "${DIR}/${name}/.brightspot-theme.properties"

gradle=${DIR}/${name}/build.gradle
echo "output $gradle"

echo " plugins {" > $gradle
echo "    id 'com.github.node-gradle.node'" >> $gradle
echo "    id 'com.github.node-gradle.gulp'" >> $gradle
echo "}" >> $gradle
echo "" >> $gradle
echo "description = '${name}'" >> $gradle
echo "" >> $gradle
echo "System.properties.put('theme.${name}.cdnCssFiles', '/styleguide/All.min.css')" >> $gradle
echo "" >> $gradle
echo "apply from: \"https://artifactory.psdops.com/psddev-releases/com/psddev/brightspot-gradle-plugins/express-theme/1.0.16/express-theme.gradle\"" >> $gradle

echo "$theme has been created, add your new theme to settings.gradle and your site/build.gradle"