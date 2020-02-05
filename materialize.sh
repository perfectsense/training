#!/usr/bin/env bash

artifact=""
group="com.psddev"
version=""
destination="core"

NC='\033[0m' # No Color
RED='\033[0;31m'
YELLOW='\033[1;33m'

for ARGUMENT in "$@"
do

    KEY=$(echo $ARGUMENT | cut -f1 -d=)
    VALUE=$(echo $ARGUMENT | cut -f2 -d=)

    case "$KEY" in
            artifact)       artifact=${VALUE} ;;
            destination)    destination=${VALUE} ;;
            group)    group=${VALUE} ;;
            version)    version=${VALUE} ;;
            *)
    esac
done

if [[ -z $artifact || -z $version ]]
then
    echo ""
    echo "Missing required parameters"
    echo "Example materialize.sh version=4.1.7.5 artfiact=express-youtube"
    echo ""
    echo "Optional arguments"
    echo "group (defaults to com.psdev)"
    exit 0;
fi

#Example ./materialize.sh version=4.2.1.1 artifact=express-autotag-comprehend
#Example ./materialize.sh version=4.1.71 group=com.psddev.component-lib artifact=amp-cors-filter

now=$(date +"%Y-%m-%d-%H:%M:%S")
log="$now $group:$artifact:$version"
echo "$now Materializing $group:$artifact:$version"

groupPath=${group//./\/}
echo $groupPath

xml=$(curl --silent https://artifactory.psdops.com/psddev-releases/com/psddev/brightspot-bom/$version/brightspot-bom-$version.pom)

realVersion=$(echo $xml | xmllint --xpath "//*[local-name()='dependency']/*[local-name()='artifactId' and text()='$artifact']/../*[local-name()='version']/text()" -
echo)

if [[ -z $realVersion ]]
then
    #Not an express component using $version instead
    realVersion=$version
fi

echo "Real version $realVersion"

mkdir "$destination/build"
mkdir "$destination/build/tmp"

srcPom="https://artifactory.psdops.com/psddev-releases/$groupPath/$artifact/$realVersion/$artifact-$realVersion.pom"

response=$( curl --write-out "%{http_code}\n" --silent --output /dev/null "$srcPom")

if [[ $response == "404" ]]
then
  echo -e "${RED}ERROR${NC} Unable to materialize artifact $artifact. Artifact not found in artifactory at $srcPom"
else

  echo "src POM $srcPom"

  destPom="build/tmp/$artifact-$realVersion-pom.xml"
  curl -o "$destination/$destPom" $srcPom

  echo "Updating gradle build ./gradlew -p $destination materialize -Dmodule=$destination -Dartifact=$artifact -DsrcPom=$destPom"
  ./gradlew -p $destination materialize -Dmodule=$destination -Dartifact=$artifact -DsrcPom="/$destPom"

  srcFile="https://artifactory.psdops.com/psddev-releases/$groupPath/$artifact/$realVersion/$artifact-$realVersion-sources.jar"
  echo "source $srcFile"

  destFile="$destination/build/tmp/$artifact-$realVersion-sources.jar"
  echo "downloading $srcFile"

  curl -o $destFile $srcFile

  echo "Copying Java"
  tar -xf $destFile --directory $destination/src/main/java *.java

  mkdir $destination/src/main/resources

  echo "Copying resources"
  tar -xf $destFile --directory $destination/src/main/resources --exclude=*.java --exclude=*.md --exclude=*.MF

  echo $log >> "$destination/materialization.log"

  #Delete empty folders
  find $destination -type d -empty -delete

  #Chedk build.gradle for other express components
  gradleBuild="core/build.gradle"
  while IFS= read -r line
  do
      if [[ $line == *"compile 'com.psddev:express"* ]]; then

          IFS=' ' read -r -a module <<< "$line"

          module=${module[1]}
          module="${module//\'}"

          IFS=':' read -r -a component <<< "$module"

          expressModule=${component[1]}
          echo -e "${YELLOW}Found Express Module ${expressModule}${NC}, you may want to materialize that as well ex: ./materialize.sh version=${brightspot} artifact=${expressModule}"
      fi
  done < "$gradleBuild"
fi
