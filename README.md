# Brightspot Training

So you want to learn how to develop on Brightspot? Look no further!

This project gives you everything you will need to get going with learning how to do front-end and back-end development on
Brightspot. It includes a bare-bones project and all of the core Styleguide templates to use as reference
to help you take your own web publishing needs from concept to creation.

## Prerequisites

- VirtualBox: `5.2.20+` - [Download](https://www.virtualbox.org/wiki/Downloads)
- Vagrant: `2.2.0+` - [Download](https://www.vagrantup.com/downloads.html)

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

#### To install binaries that are zipped upon download, follow the next few steps:

Add your local binaries directory to your PATH environment variable if you haven't already.
```bash
export PATH=$PATH:/usr/local/bin
```

Unzip the binaries package:

```bash
unzip {name-of-binaries-zip}.zip
```

Copy the binaries to your local bin:

```bash
cp path/to/binaries/binary /usr/local/bin/binary
```

Verify the binary is found:

```bash
which binary
```

The output should be `/usr/local/bin/binary`. If it says the command was not found, try restarting your terminal.

Brew is also useful for install

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
./gradlew build # Same as above.
./gradlew -x test # Builds everything but skips tests.
./gradlew training-site:deploy # Builds everything then deploys the WAR to your local Vagrant.
./gradlew tasks # Lists all tasks that you can run.
./gradlew -p youtube # Builds youtube
./gradlew -p youtube install #Installs youtube into your m2
```

You don't normally have to clean out your build files since Gradle can detect files that have been removed, but if you suspect a problem, you can run:

```console
./gradlew --rerun-tasks clean build
```

## Setting Up The Environment

Included with the project is the Brightspot `Vagrantfile` at the root of the project. This file contains all of the configuration needed to running the Brightspot CMS
out of the box for the example Marvel Cinematic Universe site.

Before booting up the vagrant box, you should clear your box cache to ensure you are importing the most up-to-date Brightspot Training box:

```bash
vagrant box remove brightspot
```

Now, boot up the vagrant:

```bash
vagrant up
```

When you boot up the vagrant box, it will attempt to mount the current directory the `Vagrantfile` is
located in to the directory `/vagrant` within the environment.

It is very simple to SSH into the training environment. From the same folder as your `Vagrantfile`, run this command:

```bash
vagrant ssh
```

You will now be logged in as the root user within the training vagrant. If you are not, log in as the root user:

```bash
sudo -i
```

During development, you may want to keep yourself SSH'd into the training environment with logs open for debugging. You can tail the
Catalina logs and leave them open in the window:

```bash
tail -f /servers/brightspot/logs/catalina.out
```

Now that your vagrant is configured, let's deploy a build to it. On your local machine, run the following command:

```bash
./gradlew training-site:deploy
```

If you're running Mac OSX Catalina or there is some other reason that your WAR file doesn't deploy, you can SSH into your vagrant and use a symlink to point to your WAR file instead:

```bash
ln -s  /vagrant/site/build/libs/training-site-1.0.0-SNAPSHOT.war /servers/brightspot/webapps/ROOT.war
```

## Accessing the CMS

Now that the Brightspot service is running you can access the cms by navigating to:

```bash
http://172.28.128.101/cms
```

## Contributing

Please refer to [Building The Training Vagrant](docs/BUILDING.md) on instructions and information on how to contribute
to the training project.

## Credits

Written and Published by [Mark Conigliaro](https://github.com/markconigliaro1) (Software Engineer at Perfect Sense Digital)

Brightspot Platform and Frost theme written and developed by Perfect Sense Digital







