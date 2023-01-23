#!/bin/sh

set -e -u

brightspotVersion=""
goVersion=""
name=""
bundle=
version=""

ARTIFACTORY="https://artifactory.psdops.com/psddev-releases"
BRIGHTSPOT_START_BOM_PATH="${ARTIFACTORY}/com/brightspot/go/bom"

for ARGUMENT in "$@"
do

    KEY=$(echo $ARGUMENT | cut -f1 -d=)
    VALUE=$(echo $ARGUMENT | cut -f2 -d=)

    case "$KEY" in
            brightspotVersion)    brightspotVersion=${VALUE} ;;
            goVersion)    goVersion=${VALUE} ;;
            bundle)    bundle=${VALUE} ;;
            version)    version=${VALUE} ;;
            name)    name=${VALUE} ;;
            *)
    esac
done

if [[ -z $name  ]] || [[ -z $goVersion ]] || [[ -z $brightspotVersion ]]
then
    echo ""
    echo "Name, goVersion and brightspotVersion required"
    echo "Example createbundle.sh name=project-bundle-test brightspotVersion=4.2.15 goVersion=1.0"
    echo ""
    echo "Optional arguments bundle=frontend-bundle-default"
    echo "bundle (defaults to frontend-bundle-default)"
    echo "version (defaults to latest)"
    echo "Example createbundle.sh name=project-bundle-test t brightspotVersion=4.2.15 goVersion=1.0 bundle=frontend-bundle-default version=4.1.5"
    echo ""
    exit 0;
fi

os="ubuntu"
if [[ "$OSTYPE" == "darwin"* ]]; then
	os="osx"
fi

if [[ -z $version ]]; then
      echo "${BRIGHTSPOT_START_BOM_PATH}/${goVersion}/bom-${goVersion}.pom"
      xml=$(curl --silent "${BRIGHTSPOT_START_BOM_PATH}/${goVersion}/bom-${goVersion}.pom")
      if [[ ${xml} != *"File not found"* ]]; then
        version=$(
          echo "${xml}" | xmllint --xpath "//*[local-name()='dependency']/*[local-name()='artifactId' and text()='${bundle}']/../*[local-name()='version']/text()" -
          echo
        )
    fi
fi

if [[ ${version} == 0 ]]; then
  echo "Unable to automatically pick the latest bundle version, please provide."
  exit 0;
fi


echo ""
echo "Building bundle '${name}' from ${bundle} version ${version}"
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

echo "Directory $DIR"

mkdir -p $DIR/$name
echo "Download bundle https://artifactory.psdops.com/psddev-releases/com/brightspot/go/$bundle/$version/$bundle-${version}.tar"
curl -SL  https://artifactory.psdops.com/psddev-releases/com/brightspot/go/$bundle/$version/$bundle-${version}.tar | tar -xf - -C $DIR/$name/ --strip-components=1

sed -i "" "s/brightspot-theme-core/${name}/g" ${DIR}/${name}/package.json

echo "# DO NOT REMOVE - Required for JarBundleInitializer to find bundle" > "${DIR}/${name}/.brightspot-theme.properties"

# Only for Gradle projects
if [ -f "../../build.gradle" ]; then
    gradle=${DIR}/${name}/build.gradle
    echo "output $gradle"

    echo " plugins {" > $gradle
    echo "    id 'com.github.node-gradle.node'" >> $gradle
    echo "    id 'com.github.node-gradle.gulp'" >> $gradle
    echo "}" >> $gradle
    echo "" >> $gradle
    echo "description = '${name}'" >> $gradle
    echo "" >> $gradle
    echo 'apply from: "https://artifactory.psdops.com/psddev-releases/com/psddev/brightspot-gradle-plugins/express-theme/${brightspotGradlePluginVersion}/express-theme.gradle"' >> $gradle

    awk "FNR==NR{ if (/include\(/) p=NR; next} 1; FNR==p{ print \"include(':${name}')\" }" ../../settings.gradle ../../settings.gradle > ../../settings.gradle.tmp
    awk "FNR==NR{ if (/project\(/) p=NR; next} 1; FNR==p{ print \"project(':${name}').projectDir = file('frontend/bundles/${name}')\" }" ../../settings.gradle.tmp ../../settings.gradle.tmp > ../../settings.gradle
    rm ../../settings.gradle.tmp

    # Update site/build.gradle
    awk "FNR==NR{ if (/    api project\(/) p=NR; next} 1; FNR==p{ print \"    api project(':${name}')\" }" ../../web/build.gradle ../../web/build.gradle > ../../web/build.gradle.tmp
    mv ../../web/build.gradle.tmp ../../web/build.gradle
fi

echo "$bundle has been created, rebuild your project."
