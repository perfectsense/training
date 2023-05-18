# Brightspot Training

So you want to learn how to develop on Brightspot? Look no further!

This project gives you everything you will need to get going with learning how to do frontend and backend development 
on Brightspot. It includes a bare-bones project and all the core Styleguide templates to use as reference to help you 
take your own web publishing needs from concept to creation.

Since with Brightspot backend and frontend development can proceed independently of one another, this guide covers 
[Backend](#backend) and [Frontend](#frontend) separately. Of course if you are going to do full-stack development you
should follow the steps for both.


## Backend

### Setup

#### Mac

Install [Homebrew](https://brew.sh/) if you don't already have it:
```console
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

Install OpenJDK 11:
```
brew install openjdk@11
```

Install [Docker Desktop](https://www.docker.com/products/docker-desktop).

#### Windows

We recommend using [Git for Windows](https://git-scm.com/downloads).

Install [OpenJDK](https://adoptium.net/releases.html?variant=openjdk11&jvmVariant=hotspot).

Install [Docker Desktop](https://www.docker.com/products/docker-desktop).

### Building the project

Brightspot projects use Gradle for dependency management and build configurations. To the build the project run from 
the root directory of the project:
```console
./gradlew
```

Here are some helpful Gradle commands:
```console
./gradlew          # Builds everything
./gradlew -x test  # Builds everything but skips tests
./gradlew tasks    # Lists all tasks that you can run
```

You don't normally have to clean out your build files since Gradle can detect files that have been removed, but if you 
suspect a problem, you can run:
```console
./gradlew --rerun-tasks clean build
```

### Running Docker

Included with the project is the Brightspot [`docker-compose.yml`](docker-compose.yml) file at the root of the project. 
This file contains all the configuration needed to run the Brightspot CMS out of the box for the example Inspire 
Confidence site. We recommend allocating at least 2GB of memory to Docker.

Run from the root directory of the project:
```console
docker-compose up -d  # create the Brightspot Docker container
```

Here are some helpful Docker commands:
```console
docker-compose logs -f  # tail the log
docker-compose stop     # stop the Brightspot Docker container
docker-compose start    # start the Brightspot Docker container
docker-compose down -v  # permanently destroy the Brightspot Docker container and all data
```

Further instructions and tips can be found at the [Brightspot Docker container site](https://hub.docker.com/r/brightspot/tomcat).

### Accessing the CMS

After starting the Brightspot Docker container you can access the CMS by navigating to http://localhost/cms. You can 
log in to the CMS using any username and password: a new account will be created automatically for any credentials you
enter.


## Frontend

### Setup

#### Mac

Install [Homebrew](https://brew.sh/) if you don't already have it:
```
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

Install [Node](https://nodejs.org/en/):
```
brew install node@16
```

Install [Yarn](https://yarnpkg.com/):
```
npm install --global yarn
```

#### Windows

Install [Node](https://nodejs.org/en/) — we recommend using the latest LTS release, currently 16.13.0.

Install Yarn:
```
npm install --global yarn
```

### Building the project

The first time you build the Styleguide application you will need to run from the 
`frontend/bundles/bundle-default/` directory of the project:
```console
yarn
```

After that, run from the `frontend/bundles/bundle-default/` directory of the project:
```console
yarn server:styleguide
```

You can access the Styleguide application at http://localhost:8080.
