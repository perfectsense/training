#!/bin/sh
#
# PRECONDITIONS
#
# 1. Java 11 compiler active
# 2. Docker daemon active
# 2. Required (by Docker) localhost ports are available
#
# POSTCONDITIONS
#
# 1. mysql.sql.gz, solr.tar.gz, and tomcat-storage.tar.gz created in directory
#    where script was run


set -eu

output_dir="$(pwd -P)"
script_dir="$(CDPATH='' cd -- "$(dirname -- "$0")" && pwd -P)"

if [ $# != 1 ]; then
  echo "USAGE: $0 <Inspire Confidence export file>" >&2
  exit
fi

ic_export_file="$1"

if [ ! -f "$ic_export_file" ]; then
  echo "$ic_export_file is not a regular file" >&2
  exit 1
fi

repo_dir="$(mktemp -dt training_repo_XXXXXX)"
absolute_repo_dir="$(CDPATH='' cd -- "$repo_dir" && pwd -P)"
trap '[ -e "$absolute_repo_dir" ] && (cd -- "$absolute_repo_dir" && docker-compose down -v || true) && rm -rf -- "$absolute_repo_dir"' EXIT

git clone -b go --depth 1 -- 'https://github.com/perfectsense/training' "$repo_dir" # TODO default branch instead
cp "$ic_export_file" "$repo_dir/export-training.json"

# can't use existing docker-compose.yml as it already has preloaded data
cp "$script_dir/import-docker-compose.yml" "$repo_dir/docker-compose.yml"

cd -- "$repo_dir"

./gradlew -x test -p web clean build

echo "Starting Docker in $(pwd)" >&2
docker-compose up -d
echo 'Press ENTER when tomcat has started (try loading http://localhost/cms)' >&2
read -r # wait for input

curl 'http://localhost/_debug/code' \
    -H 'Content-Type: application/x-www-form-urlencoded' \
    --compressed \
    --silent \
    --data-raw '_isFrame=true&action=run&type=Java&file=&jspPreviewUrl=' \
    --data-urlencode "code@${script_dir}/src/main/java/etc/Setup.java" \
  | grep --quiet 'Setup and import started' || { echo 'Failed to run Setup.java on localhost' >&2 && exit 1 ; }

echo "Press ENTER when import has completed (watch status at http://localhost/_debug/task)" >&2
read -r # wait for input

docker-compose stop tomcat
# tomcat's entrypoint precludes running sh -c, so have to reshuffle things a bit to execute tar with its args
docker-compose run \
  --entrypoint tar \
  --rm \
  --volume="$output_dir:/export" \
  tomcat \
  -c -f /export/tomcat-storage.tar.gz -z -C /servers/tomcat/storage .

# have to use running container as mysqldump needs running mysql instance to export the database
docker-compose exec mysql sh -c 'exec /servers/mysql56/bin/mysqldump -uroot -p"$MYSQL_ROOT_PASSWORD" --single-transaction brightspot | gzip --best > /export/mysql.sql.gz'
mv "$repo_dir/mysql.sql.gz" "$output_dir/"

docker-compose stop solr
docker-compose run \
  --rm \
  --volume="$output_dir:/export" \
  solr \
  sh -c 'tar --create -C /var/solr/data/collection1/data . | gzip --best > /export/solr.tar.gz'

docker-compose down -v
