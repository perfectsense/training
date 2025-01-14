# Brightspot Training

So you want to learn how to develop on Brightspot? Look no further!

This project provides everything needed
to learn frontend and backend development on Brightspot.
It includes a bare-bones project and all the core Styleguide templates
to use as reference to help you take your own web publishing needs from concept to creation.

Since with Brightspot backend and frontend development can proceed independently of one another,
this guide covers [Backend](#backend) and [Frontend](#frontend) separately.
Of course if you are going to do full-stack development you should follow the steps for both.


## Backend

### Setup

Building the backend code requires a JDK which supports Java 11.
We recommend [OpenJDK](https://adoptium.net/releases.html?variant=openjdk11&jvmVariant=hotspot&package=jdk).

Running the Docker image requires [Docker Desktop](https://www.docker.com/products/docker-desktop).

### Building the project

Brightspot projects use Gradle for dependency management and build configurations.
To build the project, run from the root directory of the project:
```console
./gradlew
```

Note that on Windows you would use `gradlew.bat` rather than `./gradlew` for all the commands mentioned in this section.

Here are some helpful Gradle commands:
```console
./gradlew                 # Builds everything
./gradlew -x test         # Builds everything but skips tests
./gradlew frontend:build  # Builds the view interfaces
./gradlew tasks           # Lists all tasks that you can run
```

You don't normally have to clean out your build files since Gradle can detect files that have been removed,
but if you suspect a problem, you can run:
```console
./gradlew clean build --no-build-cache
```

### Running Docker

Included with the project is a [`docker-compose.yml`](docker-compose.yml) file at the root of the project.
This file contains all the configuration needed to run the Brightspot CMS out of the box for the example recipe site.
We recommend [allocating at least 2GB of memory to Docker](https://docs.docker.com/desktop/settings-and-maintenance/settings/#advanced).

Run from the root directory of the project:
```console
docker compose up -d  # create the Brightspot Docker container
```

Here are some helpful Docker commands:
```console
docker compose logs tomcat -f  # tail the tomcat logs
docker compose stop            # stop the Brightspot Docker container
docker compose start           # start the Brightspot Docker container
docker compose down -v         # permanently destroy the Brightspot Docker container and all data
```

Further instructions and tips can be found at the [Brightspot Docker container site](https://hub.docker.com/r/brightspot/tomcat).

### Accessing the CMS

After starting the Brightspot Docker container you can access the CMS by navigating to <http://localhost/cms>.
You can log in to the CMS using any username and password:
a new account will be created automatically for any credentials you enter.


## Frontend

### Setup

Building the frontend and running the Styleguide application requires
[Node](https://nodejs.org/en/) 18.17.1.

Additionally, you will need [Yarn](https://yarnpkg.com/) 1.22.19.
```
npm install --global yarn@1.22.19
```

You may wish to use [nvm](https://github.com/nvm-sh/nvm) to manage your Node installation
if you require other versions as well.

### Building the project

The first time you build the Styleguide application you will need to run from the
`frontend/bundles/default/` directory of the project:
```console
yarn
```

After that, run from the `frontend/bundles/default/` directory of the project:
```console
yarn server:styleguide
```

You can access the Styleguide application at <http://localhost:8080>.
