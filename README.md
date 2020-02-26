# Brightspot Training

So you want to learn how to develop on Brightspot? Look no further!

This project gives you everything you will need to get going with learning how to do front-end and back-end development on
Brightspot. It includes a bare-bones project and all of the core Styleguide templates to use as reference
to help you take your own web publishing needs from concept to creation.

## Prerequisites

- Docker Desktop for Windows or Mac [Download](https://www.docker.com/products/docker-desktop)

- Brew (Homebrew for Mac) - Install using the following command:

```bash
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

- Node `9.8.0` - Install using the following command (requires `brew`):

```bash
brew install node
```

- Yarn: - Install using the following command (requires `brew`):

```bash
brew install yarn
```

- Java 1.8 [Download](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)

## Prepping The Local Project

On a Mac (Windows instructions coming soon), open the `Terminal` app. Change your working directory to a location where you want the project code to
live. For this example, we will use `~/Documents`.

```bash
cd ~/Documents
```

Clone the GitHub project into your `~/Documents` folder using the following command:

```bash
git clone https://github.com/perfectsense/training.git
```

Change your directory to the project:

```bash
cd training
```

## Building The Project For The First Time

Brightspot projects use Gradle for dependency management and build configurations. To the build the project for the first time:

```bash
./gradlew
```

Here are some ways to run Gradle:

```console
./gradlew # Builds everything.
./gradlew -x test # Builds everything but skips tests.
./gradlew tasks # Lists all tasks that you can run.
```

You don't normally have to clean out your build files since Gradle can detect files that have been removed, but if you suspect a problem, you can run:

```console
./gradlew --rerun-tasks clean build
```

## Setting Up The Environment

Included with the project is the Brightspot `Dockerfile` and
`docker-compose.yml` at the root of the project. This file contains all of the
configuration needed to run the Brightspot CMS out of the box for the example
Marvel Cinematic Universe site.

Further instructions and tips can be found at the [Brightspot docker container site](https://hub.docker.com/r/brightspot/brightspot).

## Accessing the CMS

Now that the Brightspot service is running you can access the cms by navigating to:

```bash
http://localhost/cms
```
