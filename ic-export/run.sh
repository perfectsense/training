#!/bin/sh
#
# PRECONDITIONS
#
# 1. Brightspot Docker mysql container has been started successfully
# 2. Have an LDAP account with at least developer access level to the
#    'Brightspot Platform' project
# 3. Required (by Docker) localhost ports are available
#
# POSTCONDITIONS
# 1. export-training.json created in directory where script was run
#
# NOTES
#
# Tested with Brightspot repo at version 4.5.7.1


set -eu

output_dir="$(pwd -P)"
script_dir="$(CDPATH='' cd -- "$(dirname -- "$0")" && pwd -P)"

copy_files() {
  bsp_repo_dir="$1"

  if [ ! -d "$bsp_repo_dir" ]; then
    echo "$bsp_repo_dir is not a directory" >&2
    exit 1
  fi

  rsync \
    --compress-level=0 \
    --executability \
    --ignore-times \
    --one-file-system \
    --quiet \
    --recursive \
    --relative \
    --times \
    --whole-file \
    "$script_dir/./src/" \
    "$bsp_repo_dir/site"
}

restore_ic_data() {
  bsp_repo_dir="$1"
  ldap_user="$2"
  project="$3"
  account="$4"

  cd -- "$bsp_repo_dir"
  docker-compose stop
  echo 'Choose the "backup" layer when prompted'
  docker-compose run mysql restore mysql -e production --user "$ldap_user" --project "$project" --account "$account"
}

run_ic_docker() {
  bsp_repo_dir="$1"

  cd -- "$bsp_repo_dir"
  ./gradlew -x test -p site build
  docker-compose up -d
}

export_data() {
  output_file="$1"

  echo 'Press ENTER when tomcat has started (try loading http://localhost/cms)' >&2
  read -r # wait for input
  curl http://localhost/export-training > "$output_file"
}

if [ $# != 4 ]; then
  echo "USAGE: $0 <Brightspot repo dir> <LDAP username> <project> <account>" >&2
  exit
fi

restore_ic_data "$1" "$2" "$3" "$4"
copy_files "$1"
run_ic_docker "$1"
export_data "$output_dir/export-training.json"
