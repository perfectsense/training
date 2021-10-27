#!/usr/bin/env bash
trap "exit 1" TERM
export TOP_PID=$$

materializeVersion="3.0.1"

# Parameters
artifact=""
group="com.brightspot.go"
version=""
destination="core"
silent="false"
recursive="true"
allowrematerialize="false"
skipbuild="false"
patch="true"
skiptests="false"
autocommit="true"
forcecommit="false"
debug="false"

# Constants
ARTIFACTORY="https://artifactory.psdops.com/psddev-releases"
BRIGHTSPOT_BOM_PATH="${ARTIFACTORY}/com/psddev/brightspot-bom"
BRIGHTSPOT_EXPRESS_BOM_PATH="${ARTIFACTORY}/com/psddev/express-bom"
BRIGHTSPOT_GO_BOM_PATH="${ARTIFACTORY}/com/brightspot/go/bom"
COMPONENT_LIB_BOM_PATH="${ARTIFACTORY}/com/psddev/component-lib/bom"
NO_COLOR=$'\033[0m' # No Color
RED=$'\033[0;31m'
YELLOW=$'\033[1;33m'
GREEN=$'\033[0;32m'
CYAN=$'\033[0;46m'

DEBUG="${CYAN}DEBUG:${NO_COLOR}"
INFO="${GREEN}INFO:${NO_COLOR}"
WARNING="${YELLOW}WARNING:${NO_COLOR}"
ERROR="${RED}ERROR:${NO_COLOR}"
EXECUTABLE=$(basename "${0}")

# Global Variables
artifactsToMaterialize=()
detectedExpressDependencies=()
materializeAll="false"
materializedItems=()
modulesToPatch=()
patchedCommitMessage=""
rootItemIsPack="false"
shouldPatch="true"
frontendDependencyDetected="false"
project=$(basename $(pwd))

#
# Parse Arguments
#

for ARGUMENT in "$@"; do

  KEY=$(echo "${ARGUMENT}" | cut -f1 -d=)
  VALUE=$(echo "${ARGUMENT}" | cut -f2 -d=)

  case "$KEY" in
    artifact) artifact=${VALUE} ;;
    destination) destination=${VALUE} ;;
    group) group=${VALUE} ;;
    version) version=${VALUE} ;;
    silent) silent=${VALUE} ;;
    recursive) recursive=${VALUE} ;;
    allowrematerialize) allowrematerialize=${VALUE} ;;
    skipbuild) skipbuild=${VALUE} ;;
    patch) patch=${VALUE} ;;
    skiptests) skiptests=${VALUE} ;;
    autocommit) autocommit=${VALUE} ;;
    forcecommit) forcecommit=${VALUE} ;;
    debug) debug=${VALUE} ;;
    *) ;;
  esac
done

if [[ -z $artifact || -z $version || -z $project ]]; then
  echo ""
  echo "usage: ${EXECUTABLE} project=$project version=\$version artifact=\$artifact [group=\$groupId destination=\$destinationDirectory silent=[true|false] recursive=[true|false] allowrematerialize=[true|false] skipbuild=[true|false] skiptests=[true|false] autocommit=[true|false]]"
  echo ""
  echo "EXAMPLES"
  echo "  ${EXECUTABLE} project=myproject version=4.1.7.5 artifact=express-youtube"
  echo "  ${EXECUTABLE} project=myproject version=4.1.195 group=com.psddev.component-lib artifact=search3-express skipbuild=true"
  echo "  ${EXECUTABLE} project=myproject version=4.1.195 group=com.psddev.component-lib artifact=search3-express silent=true"
  echo ""
  echo "Required arguments"
  echo "  version=\$version"
  echo "      The version of either the brightspot BOM, the component-lib BOM, or the artifact to materialize"
  echo "  artifact=\$artifactId"
  echo "      The artifactId of the artifact to materialize"
  echo ""
  echo "Optional arguments"
  echo "  group=\$groupId (defaults to com.psddev)"
  echo "      The groupId of the artifact to materialize."
  echo "  destination=\$destinationDirectory (defaults to core)"
  echo "      The directory into which to materialize the artifact."
  echo "  silent=[true|false] (defaults to false)"
  echo "      Whether or not to prompt for user input during materialization."
  echo "  recursive=[true|false] (defaults to true)"
  echo "      Whether or not to automatically materialize transitive dependencies (only works if silent=true)."
  echo "  allowrematerialize=[true|false] (defaults to false)"
  echo "      Whether or not to allow re-materialization of transitive dependencies (only works if silent=false)."
  echo "  skipbuild=[true|false] (defaults to false)"
  echo "      Whether or not to build the project after materialization."
  echo "  skiptests=[true|false] (defaults to false)"
  echo "      Whether or not to skip tests during the test build after materialization (only works if skipbuild=false)."
  echo "  autocommit=[true|false] (defaults to false)"
  echo "      Whether or not to automatically create a git commit of the materialized code (will not create a commit if build fails and silent=true)."
  echo "  forcecommit=[true|false] (defaults to false)"
  echo "      Whether or not to force creation of a git commit of the materialized code if the build fails (only works if autocommit=true and silent=true)."
  echo "  debug=[true|false] (defaults to false)"
  echo "      Whether or not to provide additional debug output to stdErr."
  exit 0
fi

if [[ "${group}" == "com.brightspot.go" && ${artifact} == "pack-"* ]]; then
  rootItemIsPack="true"
fi

function exitScript() {
   kill -s TERM $TOP_PID
}

echoToStdErr() {
  local message
  message="${1}"
  if [[ "${debug}" == "true" ]]; then
    echo "${message}" 1>&2
  fi
}

echoMessage() {
  local message
  message="${1}"
  echo "${message}" 1>&2
}

echoGreen() {
  local message
  message="${1}"
  printf "\x1b[32m${message}\x1b[0m\n" 1>&2
}

echoLightGreen() {
  local message
  message="${1}"
  printf "\x1b[92m${message}\x1b[0m\n" 1>&2
}

yesNoPrompt() {
  local prompt
  prompt="${1}"

  local defaultResponse
  defaultResponse=${2:-"false"}

  local defaultResponseStatus
  if [[ "${defaultResponse}" == "true" ]]; then
    defaultResponseStatus=0
  else
    defaultResponseStatus=1
  fi

  if [[ -z ${prompt} ]] || [[ ${silent} == "true" ]]; then
    return ${defaultResponseStatus}
  fi

  echo ""
  local response
  read -p "${prompt} (y/n): " -r response
  response=$(echo "${response}" | tr '[:lower:]' '[:upper:]')
  if [[ ${response} == "YES" ]] || [[ ${response} == "Y" ]]; then
    return 0
  else
    return 1
  fi
}

yesNoAllPrompt() {
  local prompt
  prompt="${1}"

  local defaultResponse
  defaultResponse=${2:-"false"}

  local defaultResponseStatus
  if [[ "${defaultResponse}" == "true" ]]; then
    defaultResponseStatus=0
  else
    defaultResponseStatus=1
  fi

  if [[ -z ${prompt} ]] || [[ ${silent} == "true" ]]; then
    return ${defaultResponseStatus}
  fi

  if [[ ${materializeAll} == "true" ]]; then
    return 0
  fi

  echo ""
  local response
  read -p "${prompt} (yes/no/all): " -r response
  response=$(echo "${response}" | tr '[:lower:]' '[:upper:]')
  if [[ ${response} == "YES" ]] || [[ ${response} == "Y" ]]; then
    return 0
  else
    if [[ ${response} == "ALL" ]] || [[ ${response} == "A" ]]; then
      materializeAll="true"
      return 0
    else
      return 1
    fi
  fi
}

checkVersion() {
  echo "${INFO} Materialize version ${materializeVersion}"
  latestMaterializeVeresion=$(curl --silent "https://s3.amazonaws.com/psddev/go-materailize-latest-version.txt")
  materializeVersionInfo=()
  IFS=$'\n' read -rd '' -a materializeVersionInfo <<<"$latestMaterializeVeresion"

  if [[ "${materializeVersion}" != "${materializeVersionInfo[0]}" ]]; then
    echo "${INFO} A newer version is avialable ${materializeVersionInfo[0]}"
    echo "${INFO} ${materializeVersionInfo[1]}"

    if yesNoPrompt "Would you like to download it into the current directory: ${PWD}?"; then
      curl -o materialize.sh ${materializeVersionInfo[1]}
      echo "Updated version has finished downloading, please rerun your materialization command"
      exitScript
    fi
  else
    echo "${INFO} You are on the latest version"
  fi
}

addPackItemToMaterializedItemsList() {
  local packArtifact="${1}"
  local packVersion="${2}"

  #Look up and add pack's assets
  packedItemsUrl="https://artifactory.psdops.com/psddev-releases/com/brightspot/go/${packArtifact}/${packVersion}/${packArtifact}-${packVersion}.jar!/packed-items.txt"
  items=$(curl --silent "${packedItemsUrl}")
  if [[ ${items} != *"File not found"* ]]; then
    SAVEIFS=$IFS   # Save current IFS
    IFS=$'\n'      # Change IFS to new line
    lines=($items)
    IFS=$SAVEIFS   # Restore IFS

    for (( i=0; i<${#lines[@]}; i++ ))
    do
      local artifact=${lines[$i]}
      materializedItems+=("com.brightspot.go:${artifact}")
    done

  else
    echo "${ERROR} Could not find Pack dependencies at ${packPomUrl}. Aborting."
    exit 1
  fi
}

parseMaterializationLog() {

  local destinationDirectory="${1}"
  if [[ -z "${destinationDirectory}" ]]; then
    echo "${WARNING} Failed to parse materialization.log: ${destinationDirectory} is empty or doesn't exist!"
    return 1
  fi

  # List of previously materialized components
  local materializationLog
  materializationLog="${destinationDirectory}/materialization.log"

  if [[ -f "${materializationLog}" ]]; then
    while IFS=$'\n' read -r line; do
      IFS=' ' read -r -a modules <<< "$line"

      module=${modules[1]}
      IFS=':' read -r -a component <<< "${module}"

      materializedArtifact="${component[0]}:${component[1]}"

      materializedItems+=("${materializedArtifact}")

      # Pack detection
      if [[ "${materializedArtifact}" == "com.brightspot.go:pack-"* ]]; then
        addPackItemToMaterializedItemsList ${component[1]} ${component[2]}
      fi

    done < "${materializationLog}"
  fi
}

function alreadyMaterialized() {
  local artifactId="${1}"
  local groupId="${2}"

  local dependency="${groupId}:${artifactId}"

  for materializedItem in "${materializedItems[@]}"; do
    if [[ "${materializedItem}" == "${dependency}" ]]; then
      return 0
    fi
  done
  return 1
}

function alreadyDetected() {
  local artifactId="${1}"
  local groupId="${2}"

  local dependency="${groupId}:${artifactId}"

  for expressItem in "${detectedExpressDependencies[@]}"; do
    if [[ "${expressItem}" == "${dependency}" ]]; then
      return 0
    fi
  done
  return 1
}

function shouldWePatch(){
  local major="${1}"
  local minor="${2}"
  local patchVer="${3}"

  local patch
  patch="false"

  # Patching only applies to Brightspot and Component Lib
  if [[ $group == "com.psddev" ]] || [[ $group == "com.psddev.component-lib" ]]; then
    if [[ $major -gt 4 ]]; then
      patch="true";
    else
      if [[ $major == 4 ]]; then
        if [[ $minor -ge 3 ]]; then
          patch="true";
        else
          if [[ $minor == 2 ]]; then

            if [[ $patchVer == *"-"* ]]; then
              patchParts=( ${patchVer//-/ } )
              patpatchVerch=${patchParts[0]}
            fi

            if [[ $group == "com.psddev.component-lib" ]] && [[ $patchVer -ge 50 ]]; then
              patch="true"
            else
              if [[ $patchVer -ge 11 ]]; then
                patch="true";
              fi
            fi
          fi
        fi
      fi
    fi
  fi
  echo "${patch}"
}

function determineRealVersion() {
  local artifactId=${1}
  local groupId=${2}
  local providedVersion=${3}

  echoToStdErr "${DEBUG} Determining real version number for ${groupId}:${artifactId} with a provided version of ${providedVersion}"
  local realVersion
  realVersion=""

  local groupPath
  groupPath="${groupId//./\/}"

  local xml
  xml=$(curl --silent "${BRIGHTSPOT_GO_BOM_PATH}/${providedVersion}/bom-${providedVersion}.pom")
  if [[ ${xml} != *"File not found"* ]]; then
    realVersion=$(
      echo "${xml}" | xmllint --xpath "//*[local-name()='dependency']/*[local-name()='artifactId' and text()='${artifactId}']/../*[local-name()='version']/text()" -
      echo
    )
    echoToStdErr "${DEBUG}   ${groupId}:${artifactId} found in Brightspot Start BOM with version ${realVersion}"
  fi

  if [[ -z "${realVersion}" ]]; then
    xml=$(curl --silent "${BRIGHTSPOT_EXPRESS_BOM_PATH}/${providedVersion}/express-bom-${providedVersion}.pom")
    if [[ ${xml} != *"File not found"* ]]; then
      realVersion=$(
        echo "${xml}" | xmllint --xpath "//*[local-name()='dependency']/*[local-name()='artifactId' and text()='${artifactId}']/../*[local-name()='version']/text()" -
        echo
      )
      echoToStdErr "${DEBUG}   ${groupId}:${artifactId} found in Brightspot Express BOM with version ${realVersion}"
    fi
  fi

  if [[ -z "${realVersion}" ]]; then
    local xml
    xml=$(curl --silent "${BRIGHTSPOT_BOM_PATH}/${providedVersion}/brightspot-bom-${providedVersion}.pom")
    if [[ ${xml} != *"File not found"* ]]; then
      realVersion=$(
        echo "${xml}" | xmllint --xpath "//*[local-name()='dependency']/*[local-name()='artifactId' and text()='${artifactId}']/../*[local-name()='version']/text()" -
        echo
      )
      echoToStdErr "${DEBUG}   ${groupId}:${artifactId} found in Brightspot BOM with version ${realVersion}"
    fi
  fi

  if [[ -z "${realVersion}" ]]; then
    #Not an express check component library
    local xml
    xml=$(curl --silent "${COMPONENT_LIB_BOM_PATH}/${providedVersion}/bom-${providedVersion}.pom")

    if [[ ${xml} != *"File not found"* ]]; then
      realVersion=$(
        echo "${xml}" | xmllint --xpath "//*[local-name()='dependency']/*[local-name()='artifactId' and text()='${artifactId}']/../*[local-name()='version']/text()" -
        echo
      )
      echoToStdErr "${DEBUG}   ${groupId}:${artifactId} found in Component-Lib BOM with version ${realVersion}"
    fi

    if [[ -z "${realVersion}" ]]; then
      # Can't find version via a BOM POM attempting to use provided $version instead
      echoToStdErr "${DEBUG}   ${groupId}:${artifactId} not found in BOM files. Falling back to ${providedVersion}"
      realVersion=${providedVersion}
    fi
  fi

  echo "${realVersion}"
}

build() {
  if [[ ${skipbuild} == "true" ]]; then
    echo "${INFO} Skipping Build"
    return 0
  else
    local args
    args=()
    if [[ ${skiptests} == "true" ]]; then
      echo "${INFO} Building (without tests)"
      args+=(-xtest)
    else
      echo "${INFO} Building (with tests)"
    fi

    if ! ./gradlew "${args[@]}"; then
      return 1
    fi
  fi

  return 0
}

buildAndCommit() {
  local groupId=${1}
  local artifact=${2}
  local version=${3}

  local buildSuccessful

  build
  buildSuccessful=$?

  while ! [[ ${buildSuccessful} -eq 0 ]]; do
    echo ""
    if yesNoPrompt "${ERROR} Build failed! Please press 'y' to re-try the build once fixed to continue with materialization, or press 'n' to abort!"; then
      echo ""
      build
      buildSuccessful=$?
    else
      echo ""
      echo "${WARNING} Aborting Materialization"
      break
    fi
  done

  # If we're not committing, we're passing back the build status immediately
  if [[ ${autocommit} != "true" ]]; then
    return ${buildSuccessful}
  fi

  # Providing the option to commit despite build failure
  if ! [[ ${buildSuccessful} -eq 0 ]]; then
    if ! yesNoPrompt "${WARNING} Do you want to commit the current status despite the build failure?" "${forcecommit}"; then
      return ${buildSuccessful}
    else
      echo "${WARNING} Forcing a git commit."
    fi
  else
    echo "${INFO} Committing to git."
  fi

  # If we get this far, autocommit is true and we either build successfully or are forcing the commit
  git add .
  commitMessage="$(printf "Materialized ${group}:${artifact}:${version}")"
  if [[ ! -z $patchedCommitMessage ]]; then
    commitMessage="$(printf "${commitMessage}\n${patchedCommitMessage}")"
  fi

  git commit -m "$commitMessage" --quiet

  return ${buildSuccessful}
}

function addArtifactToDetectedExpressDependencies() {
  local artifactId="${1}"
  local groupId="${2}"

  detectedExpressDependencies+=("${groupId}:${artifactId}")
}

function addArtifactToMaterializationQueue() {
  local artifactId="${1}"
  local groupId="${2}"
  local providedVersion="${3}"

  artifactsToMaterialize+=("${groupId}:${artifactId}:${providedVersion}")
}

#Example ./materialize.sh version=0.3.0 artifact=pack-go-basic
#Example ./materialize.sh version=4.1.71 group=com.psddev.component-lib artifact=amp-cors-filter

function processDiff() {
  local artifactId=${1}
  local dir=${2}
  local diff=${3}
  IFS=':'
  local diffComponents
  read -r -a diffComponents <<< "${diff}"
  local diffModule="${diffComponents[0]}"
  local commitMessage="${diffComponents[1]}"

  echoGreen "Attempting to apply patch $diff, message: ${commitMessage}"

  echo "Diff Module ${diff}"
  if [[ $artifactId == "express-core" ]]; then
    echo "Core diff"
    git apply $dir/$diffModule -p3  --directory=${destination} --exclude=*build.gradle --reject || true
  else
    # Determine patch level
    # p4 = diff --git a/express/search/search-sort-alphabetical/src/main/java/brightspot/search/sortalphabetical/AlphabeticalAscendingDynamicQuerySort.java
    # p3 = diff --git a/express/quiz/src/main/java/brightspot/quiz/QuizPage.java b/express/quiz/src/main/java/brightspot/quiz/QuizPage.java
    # read first line
    echo "Dir ${dir}/${diffModule}"

    line=$(head -n 1 ${dir}/${diffModule})
    lineToSrc=${line%%/src/*}
    levels=$(echo ${lineToSrc} | awk '{print gsub(/\//, "")}' )
    levels=$(( levels + 1 ))

    git apply $dir/$diffModule -p${levels} --directory=${destination} --exclude=*build.gradle --reject || true
  fi

  # Detect rejection files and prompt to fix or exit if found
  gitStatus=$(git status)

  if [[ $gitStatus == *".rej"* ]]; then
    if yesNoPrompt "${WARNING} patch Rejection files detected, try applying the patch at ${dir}/${diffModule} manually and removing the *.rej files if succesful. Please press 'y' to continue once fixed, or press 'n' to abort!"; then
      echo ""
    else
      exit 1;
    fi
  fi

  diffMessage="${diffModule}: ${commitMessage}"
  patchedCommitMessage="$(printf "${patchedCommitMessage}\n${diffMessage}")"
}


function materialize() {
  local artifactId="${1}"
  local groupId="${2}"
  local realVersion="${3}"
  local destinationDirectory="${4}"

  echo "${INFO} Materializing ${groupId}:${artifactId}:${realVersion} into ${destinationDirectory}"

  local now
  now=$(date +"%Y-%m-%d-%H:%M:%S")

  local log
  log="${now} ${groupId}:${artifactId}:${realVersion}"

  mkdir -p "${destinationDirectory}/build"
  mkdir -p "${destinationDirectory}/build/tmp"

  local groupPath
  groupPath="${groupId//.//}"

  local srcPom
  srcPom="${ARTIFACTORY}/${groupPath}/${artifactId}/${realVersion}/${artifactId}-${realVersion}.pom"

  # Determining whether artifact exists on Artifactory
  local response
  response=$(curl --write-out "%{http_code}\n" --silent --output /dev/null "${srcPom}")
  if [[ $response == "404" ]]; then
    echo -e "${ERROR} Unable to materialize ${groupId}:${artifactId}:${realVersion}. Artifact not found in Artifactory at ${srcPom}"
    return 1
  else

    local destPom="build/tmp/${artifactId}-${realVersion}-pom.xml"
    curl -o "${destinationDirectory}/${destPom}" "${srcPom}"

    echo "${INFO} Updating ${destinationDirectory}/build.gradle"
    printf -v materializedList '%s,' "${materializedItems[@]}"
    if ! ./gradlew -p "${destinationDirectory}" materialize -Dmodule="${destinationDirectory}" -Dgroup="${groupId}" -Dartifact="${artifactId}" -DsrcPom="/${destPom}" -Dmaterialized="/${materializedList}" -x compileJava; then
      echo -e "${ERROR} Updating Gradle ${destinationDirectory}/build.gradle failed for ${srcPom}."
      return 1
    fi

    local srcFile
    srcFile="${ARTIFACTORY}/${groupPath}/${artifactId}/${realVersion}/${artifactId}-${realVersion}-sources.jar"

    local destFile
    destFile="${destinationDirectory}/build/tmp/${artifactId}-${realVersion}-sources.jar"

    echo "${INFO} Downloading Source JAR ${srcFile}"
    curl --silent -o "${destFile}" "${srcFile}"

    mkdir -p "${destinationDirectory}/src/main/java"

    echo "${INFO} Copying Java Files to ${destinationDirectory}/src/main/java"
    tar -xf "${destFile}" --directory "${destinationDirectory}/src/main/java" "*.java"

    mkdir -p "${destinationDirectory}/src/main/resources"

    echo "${INFO} Copying Resource Files to ${destinationDirectory}/src/main/resources"
    tar -xf "${destFile}" --directory "${destinationDirectory}/src/main/resources" --exclude=*.java --exclude=*.md --exclude=*.MF

    if [[ ${shouldPatch} == "true" ]]; then
      patchVersion=""
      for dependency in "${modulesToPatch[@]}"; do
        groupArtifact="${groupId}:${artifactId}:"
        if [[ $dependency == "${groupArtifact}"* ]]; then
            dependencyParts=()
            IFS=':' read -r -a dependencyParts <<< "${dependency}"
            patchVersion=${dependencyParts[2]}
        fi
      done

      if [[ ! -z $patchVersion ]]; then
        groupArtifactoyrPath="${groupId}/${artifactId}"

        groupArtifactoyrPath=${groupArtifactoyrPath//./\/}

        patchUrl="https://artifactory.psdops.com/psddev-releases/com/psddev/goerkit/patches/${groupArtifactoyrPath}/${patchVersion}/${artifactId}-${patchVersion}.tgz"

        response=$(curl --write-out '%{http_code}' --silent --output /dev/null "${patchUrl}")
        if [[ $response == 200 ]]; then
          echo ""
          echoLightGreen "Attempting to apply patches for ${group}:${artifactId}"
          echo ""
          patchDest="${destinationDirectory}/build/tmp/patches/${groupArtifactoyrPath}/${realVersion}"
          mkdir -p "${patchDest}"
          patchFile="${patchDest}/${artifactId}-${patchVersion}.tgz"
          curl --silent -o "${patchFile}" "${patchUrl}"
          mkdir -p "${patchDest}/${artifactId}"
          tar -xf "${patchFile}" --directory "${patchDest}/${artifactId}"

          diffs=()
          dir="${patchDest}/${artifactId}"
          input="$dir/diff.txt"

          while IFS=$'\n' read -r line; do
            if ! [ -z "$line" ]; then
              diffs+=("$line")
            fi
          done < "$input"

          patchedCommitMessage="$(printf "Patches: ")"
          for diff in "${diffs[@]}"
          do
            processDiff $artifactId $dir "$diff"
          done
        fi
      fi
    fi

    echo "${log}" >> "${destinationDirectory}/materialization.log"

    #Delete empty folders
    find "${destinationDirectory}" -type d -empty -delete

    #Check build.gradle for other materializable components
    local gradleBuild="core/build.gradle"
    while IFS= read -r line; do
      materializable="false"
       if [[ ${line} == *"compile"* ]] || [[ ${line} == *"api"* ]]; then
         if [[ ${line} == *"com.psddev:express"* ]] || [[ ${line} == *"com.brightspot.go:site-"* ]] || [[ ${line} == *"com.brightspot.go:pack-"* ]] || [[ ${line} == *"com.brightspot.go:frontend-"* ]]; then
          materializable="true"
         fi
      fi

      if [[ ${materializable} == "true" ]]; then

        IFS=' ' read -r -a modules <<< "${line}"

        module=${modules[1]}
        module="${module}"

        IFS=':' read -r -a component <<< "${module}"
        IFS="'" read -r -a componentArtifact <<< "${component[1]}"

        local expressModule=${componentArtifact}
        echo -e "${WARNING} Found Module ${expressModule} you may want to materialize that as well"
      fi
    done < "${gradleBuild}"
  fi
}

function timeinMs() {
  echo date +%s%3N
}

function processExpressDependencies() {
  local artifactId=${1}
  local groupId=${2}
  local realVersion=${3}
  local destinationDirectory="${4}"

  echo "${INFO} ${timeinMs} Getting dependencies for ${groupId}:${artifactId}:${realVersion}"
  #  com.brightspot.go:pack-go-basic
  # auto materialize pack, frontend
  materializable="false"
   if [[ "${artifactId}" == *"express"* ]]; then
     materializable="true"
  else
    if [[ "${groupId}" == "com.brightspot.go" ]]; then
       if [[ "${artifactId}" == "site-"* ]] || [[ "${artifactId}" == "pack-"* ]] || [[ "${artifactId}" == "frontend-"* ]]; then
          materializable="true"
       fi
    fi
  fi

  if [[ ${materializable} == "true" ]]; then
    local groupPath
    groupPath="${groupId//.//}"

    local srcPom
    srcPom="${ARTIFACTORY}/${groupPath}/${artifactId}/${realVersion}/${artifactId}-${realVersion}.pom"

    echo "${INFO} ${date} Parsing ${srcPom} for express dependencies."
    local xml
    xml=$(curl --silent "${srcPom}")

    if [[ "${xml}" == *"File not found"* ]]; then
      echo -e "${ERROR} Unable to materialize artifact ${artifactId}. Artifact not found in Artifactory at ${srcPom}"
    else
      local itemsCount
      itemsCount=$(
        echo "${xml}" | xmllint --xpath "count(//*[local-name()='project']/*[local-name()='dependencies']//*[local-name()='dependency'])" -
        echo
      )

      echo "${INFO} ${timeinMs} Found ${itemsCount} dependencies that could be materialized in ${srcPom}."
      for ((i = 1; i <= itemsCount; i++)); do
        local dependencyArtifact
        dependencyArtifact=$(
          echo "${xml}" | xmllint --xpath "//*[local-name()='project']/*[local-name()='dependencies']//*[local-name()='dependency'][$i]/*[local-name()='artifactId']/text()" -
          echo
        )

        local dependencyGroup
        dependencyGroup=$(
          echo "${xml}" | xmllint --xpath "//*[local-name()='project']/*[local-name()='dependencies']//*[local-name()='dependency'][$i]/*[local-name()='groupId']/text()" -
          echo
        )

        if [[ "${dependencyGroup}" == "com.brightspot.go" && "${dependencyArtifact}" == "frontend" ]]; then
          frontendDependencyDetected="true"
        fi

        # SKIP type POM
       local materializeItem="false"
       if [[ "${dependencyArtifact}" == *"express"* && "${dependencyArtifact}" != "express-frontend" ]]; then
         materializeItem="true"
       fi

       if [[ "${dependencyGroup}" == "com.brightspot.go" ]]; then
         if [[ "${dependencyArtifact}" == "frontend"* || "${dependencyArtifact}" == "pack"* || "${dependencyArtifact}" == "site"*  ]]; then
           materializeItem="true"
         fi
       fi

       if [[ "${materializeItem}" == "true" ]]; then

          # Checking whether the dependency was already detected previously.
          # If so, we're skipping it.
          if alreadyDetected "${dependencyArtifact}" "${dependencyGroup}"; then
            continue
          fi

          # Adding artifact to detected dependencies (so we skip the dependency if encountered a second time)
          addArtifactToDetectedExpressDependencies "${dependencyArtifact}" "${dependencyGroup}"

          # Determining whether or not the dependency has already been materialized
          if alreadyMaterialized "${dependencyArtifact}" "${dependencyGroup}"; then

            # If dependency was already materialized and allowrematerialize is true, prompt for re-materialization.
            if [[ "${allowrematerialize}" != "true" ]] || ! yesNoPrompt "${WARNING} ${dependencyGroup}:${dependencyArtifact} has already been materialized. Would you like to re-materialize it?."; then
              echo "${INFO} Skipping re-materialization of ${dependencyGroup}:${dependencyArtifact}!"
              continue

            # Otherwise, skip dependency.
            else
              echo "${WARNING} Re-materializing ${dependencyGroup}:${dependencyArtifact}!"
            fi

          # Not already materialized
          else

            # Prompting whether or not the dependency should be added to the queue
            # If ${silent} is true, the value of ${recursive} determines the default behavior (defaults to true)
            if ! yesNoAllPrompt "${INFO} ${dependencyGroup}:${dependencyArtifact} hasn't been materialized yet. Would you like to materialize it?" "${recursive}"; then
              echo "${WARNING} Skipping materialization of ${dependencyGroup}:${dependencyArtifact}!"
              continue
            fi
          fi

          echo " dependencyArtifact ${dependencyArtifact}"
          # Parsing dependency version from POM file
          local dependencyVersion
          dependencyVersion=$(
            echo "${xml}" | xmllint --xpath "//*[local-name()='project']/*[local-name()='dependencies']//*[local-name()='dependency'][$i]/*[local-name()='version']/text()" -
            echo
          )

          # Determining the actual version via Artifactory
          dependencyVersion=$(determineRealVersion "${dependencyArtifact}" "${dependencyGroup}" "${dependencyVersion}")

          if [[ "${rootItemIsPack}" == "false" ]]; then
            # Processing transitive dependencies (doing this first to materialize based on post-order traversal)
            processExpressDependencies "${dependencyArtifact}" "${dependencyGroup}" "${dependencyVersion}" "${destinationDirectory}"
          fi

          # Adding the artifact to the materialization queue
          if [[ "${dependencyGroup}" != "com.brightspot.go" || "${dependencyArtifact}" != "frontend" ]]; then
            addArtifactToMaterializationQueue "${dependencyArtifact}" "${dependencyGroup}" "${dependencyVersion}"
          fi
        fi
      done
    fi
  fi
}

function process() {
  local artifactId=${1}
  local groupId=${2}
  local providedVersion=${3}
  local destinationDirectory="${4}"

  # Clear previous patchedCommitMessage
  patchedCommitMessage=""

  # Parsing the materialization log to determine all items already materialized
  parseMaterializationLog "${destinationDirectory}"

  # If the root artifact has already been materialized, prompt as to whether it should be re-materialized
  # If ${silent} is true, then it will not be re-materialized

  shouldMaterialize="true"

  if  alreadyMaterialized "${artifactId}" "${groupId}"; then
    shouldMaterialize="false"
  fi
  if [[ "${shouldMaterialize}" == "false" && "${allowrematerialize}" == "true" ]]; then
    if yesNoPrompt "${WARNING} ${groupId}:${artifactId} has already been materialized. Would you like to re-materialize it??" "true"; then
      shouldMaterialize="true"
    fi
  fi

  if  [[ "${shouldMaterialize}" == "true" ]]; then
    # Determining the "real" version of the root artifact based on BOM files
    local realVersion
    realVersion=$(determineRealVersion "${artifactId}" "${groupId}" "${providedVersion}")

    # 1. Adding artifact to detected dependencies
    # 2. Processing transitive dependencies
    # 3. Adding the artifact to the materialization queue
    addArtifactToDetectedExpressDependencies "${artifactId}" "${groupId}"
    processExpressDependencies "${artifactId}" "${groupId}" "${realVersion}" "${destinationDirectory}"
    if [[ "${groupId}" != "com.brightspot.go" || "${artifactId}" != "frontend" ]]; then
      addArtifactToMaterializationQueue "${artifactId}" "${groupId}" "${realVersion}"
    fi

    # Materialize and build every dependency that was queued

    echo "${INFO} Materializing the following artifacts:"
    for dependency in "${artifactsToMaterialize[@]}"; do
      echo "${INFO}   ${dependency}"
    done

    for dependency in "${artifactsToMaterialize[@]}"; do

      IFS=':'
      # split ${groupId}:${artifactId}:${version} into its components
      local dependencyComponents
      read -r -a dependencyComponents <<< "${dependency}"
      local dependencyGroup="${dependencyComponents[0]}"
      local dependencyArtifact="${dependencyComponents[1]}"
      local dependencyVersion="${dependencyComponents[2]}"

      # Materialize dependency
       if [[ "${groupId}" == "com.brightspot.go" && "${artifactId}" == "frontend" ]]; then
          frontendDependencyDetected="true"
        else
        if ! materialize "${dependencyArtifact}" "${dependencyGroup}" "${dependencyVersion}" "${destinationDirectory}"; then
          echo "${ERROR} Failed to materialize ${dependencyGroup}:${dependencyArtifact}:${dependencyVersion}! Aborting."
          exit 1
        fi
      fi

      # Temporary workaround to remove frontend dependency
      if [[ "${dependencyGroup}" == "com.brightspot.go" && "${dependencyArtifact}" == "frontend" ]]; then
          rm -rf ${destinationDirectory}/src/main/java/com/psddev/styleguide/
      fi

      # Build project with materialized dependency
      if ! buildAndCommit "${dependencyGroup}" "${dependencyArtifact}" "${dependencyVersion}"; then
        if ! yesNoPrompt "${ERROR} Build/commit failed for ${dependencyGroup}:${dependencyArtifact}:${dependencyVersion}! Would you like to continue materialization?"; then
          exit 1
        fi
      fi
    done
  fi
}

majorVersion=""
minorVersion=""
patchVersion=""
versionLevels=( ${version//./ } )

if [[ ${patch} == "false" ]] || [[ ${patch} == "true" ]]; then
  versionLevels=( ${version//./ } )
else
  versionLevels=( ${patch//./ } )
fi

# if Version >= 4.2.11 confirm diff patches are avaialble
if [[ ${#versionLevels[@]} -lt 3 ]]; then
  echoMessage "Unable to determine version number from ${providedVersion}"
  exit 1;
else
  majorVersion=${versionLevels[0]}
  minorVersion=${versionLevels[1]}
  patchVersion=${versionLevels[2]}
fi

if [[ ${patch} == "false" ]]; then
  shouldPatch="false"
else
  shouldPatch=$(shouldWePatch "${majorVersion}" "${minorVersion}" "${patchVersion}")
fi

if [[ ${shouldPatch} == "true" ]]; then
  patchCheckUrl=""
  if [[ $group == *"component-lib"* ]]; then
    patchCheckUrl="https://artifactory.psdops.com/psddev-releases/com/psddev/starterkit/patches/com/psddev/component-lib/${version}/diff.txt"
  else
    patchCheckUrl="https://artifactory.psdops.com/psddev-releases/com/psddev/starterkit/patches/com/psddev/brightspot/${version}/diff.txt"
  fi

  response=$(curl --write-out '%{http_code}' --silent --output /dev/null "${patchCheckUrl}")
  if [[ $response != 200 ]]; then
    # An exact version matching patch hasn't been found use the latest Major.Minor
    if [[ $group == *"component-lib"* ]]; then
      lastPatchFound="false"
      limit=100
      count=0

      while [[ ${lastPatchFound} == "false" ]]; do
        count=$((count + 1))
        if [[ count -gt limit ]]; then
          shouldPatch="false"
          break
        fi

        patchCheckUrl="https://artifactory.psdops.com/psddev-releases/com/psddev/starterkit/patches/com/psddev/component-lib/${majorVersion}.${minorVersion}.${patchVersion}/diff.txt"

        response=$(curl --write-out '%{http_code}' --silent --output /dev/null "${patchCheckUrl}")
        if [[ $response == 200 ]]; then
          lastPatchFound="true"
        else
          patchVersion=$((patchVersion - 1))
        fi
      done

    else
      patchCheckUrl="https://artifactory.psdops.com/psddev-releases/com/psddev/starterkit/patches/com/psddev/brightspot/${majorVersion}.${minorVersion}.${patchVersion}/diff.txt"

      response=$(curl --write-out '%{http_code}' --silent --output /dev/null "${patchCheckUrl}")
      if [[ $response != 200 ]]; then
        shouldPatch="false"
      fi
    fi
  fi

  if [[ ${shouldPatch} == "true" ]]; then
    response=$(curl ${patchCheckUrl})
    IFS=$'\n' read -rd '' -a modulesToPatch <<<"$response"
  fi
fi

checkVersion

if [[ "${group}" == "com.brightspot.go" && ${artifact} == "pack-"* ]]; then
  dependencyVersion=$(determineRealVersion "${artifact}" "${group}" "${version}")

  addPackItemToMaterializedItemsList ${artifact} ${dependencyVersion}
fi

process "${artifact}" "${group}" "${version}" "${destination}"

if [[ ${frontendDependencyDetected} == "true" ]]; then
  if [ ! -d "frontend" ]; then
    echo "Building frontend"
    echo "project ${project}"
    IFS=""
    coreDependency="dependencies {"
    coreNewDependency="dependencies {\r\n    api project(':${project}-frontend')"
    frontendDependency="api 'com.brightspot.go:frontend'"

    cd core
    grep -rl "${coreDependency}" build.gradle | xargs sed -i '' -e "s/${coreDependency}/""${coreNewDependency}""/"
    # Remove Brightspot Go Frontend Dependency
    grep -rl "${frontendDependency}" build.gradle | xargs sed -i '' -e "s/${frontendDependency}/""""/"
    cd ..

    cd web
    grep -rl "${coreDependency}" build.gradle | xargs sed -i '' -e "s/${coreDependency}/""${coreNewDependency}""/"
    cd ..

    siteNewInclude="include(':${project}-frontend')"
    awk 'FNR==NR{ if (/include\(/) p=NR; next} 1; FNR==p{ print "'${siteNewInclude}'" }' settings.gradle settings.gradle > tmp && mv tmp settings.gradle

    siteNewProject="project(':${project}-frontend').projectDir"
    siteNewProject2="="
    siteNewProject3="file('frontend')"
    awk 'FNR==NR{ if (/project\(/) p=NR; next} 1; FNR==p{ print "'${siteNewProject}'" " " "'${siteNewProject2}'" " " "'${siteNewProject3}'"  }' settings.gradle settings.gradle > tmp && mv tmp settings.gradle

    echo "${BRIGHTSPOT_GO_BOM_PATH}/${version}/bom-${version}.pom"
    xml=$(curl --silent "${BRIGHTSPOT_GO_BOM_PATH}/${version}/bom-${version}.pom")
    if [[ ${xml} != *"File not found"* ]]; then
      setupFrontend=$(
        echo "${xml}" | xmllint --xpath "//*[local-name()='dependency']/*[local-name()='artifactId' and text()='setup-frontend']/../*[local-name()='version']/text()" -
        echo
      )

      styleguide=$(
        echo "${xml}" | xmllint --xpath "//*[local-name()='dependency']/*[local-name()='artifactId' and text()='frontend-styleguide']/../*[local-name()='version']/text()" -
        echo
      )

      mkdir -p "core/build/tmp"

      echo "Downloading Frontend Setup from Artifactory"
      frontendUrl="https://artifactory.psdops.com/psddev-releases/com/brightspot/go/setup-frontend/${setupFrontend}/setup-frontend-${setupFrontend}.jar"
      frontendFile="core/build/tmp/setup-frontend-${setupFrontend}.jar"
      curl --silent -o "${frontendFile}" "${frontendUrl}"
      tar -xf "${frontendFile}"

      echo "Downloading Styleguide ${styleguide} from Artifactory"
      styleguideUrl="https://artifactory.psdops.com/psddev-releases/com/brightspot/go/frontend-styleguide/${styleguide}/frontend-styleguide-${styleguide}.jar"
      styleguideFile="core/build/tmp/styleguide-${styleguide}.jar"
      curl --silent -o "${styleguideFile}" "${styleguideUrl}"
      tar -xf "${styleguideFile}" --directory frontend
      [ -e styleguide/build.gradle ] && rm styleguide/build.gradle
      [ -e styleguide/build.properties ] && rm styleguide/build.properties
      [ -e styleguide/META-INF ] && rm -rf styleguide/META-INF
    fi
    git add "."
    git commit -m "Initialized Frontend"  --quiet
  fi
fi

rm -f ${destinationDirectory}/src/main/resources/packed-item.txt
