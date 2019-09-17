# Brightspot Express Training

So you want to learn how to develop on Brightspot Express? Look no further!

This project gives you everything you will need to get going with learning how to do front-end and back-end development on
Brightspot Express. It includes a bare-bones 4.0 archetype project and all of the core Styleguide templates to use as reference
to help you take your own web publishing needs from concept to creation.

## Prerequisites

- Brew (Homebrew for Mac) - Install using the following command:

```bash
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
brew install cask
```

- VirtualBox: `5.0.10+` - Install using the following command (requires `brew`):

```bash
brew cask install virtualbox
```

- Vagrant: `1.8.1+` - Install using the following command (requires `brew`):

```bash
brew cask install vagrant
```

- Maven: `3.5.2+` - Install using the following command (requires `brew`):

```bash
brew install mvn
```

- Node `9.8.0` - Install using the following command (requires `brew`):

```bash
brew install node
```

- Gulp: `3.9.1` - Install using the following command (requires `npm`):

```bash
npm install gulp -g
``` 

- Yarn: `1.5.1` - Install using the following command (requires `brew`):

```bash
brew install yarn
```

## Prepping The Local Project

On a Mac (Windows instructions coming soon), open the `Terminal` app. Change your working directory to a location where you want the project code to
live. For this example, we will use `~/Documents`.

```bash
cd ~/Documents
```

Clone the GitHub project into your `~/Documents` folder using the following command:

```bash
git clone git@github.com:perfectsense/training.git
```

Change your directory to the project:

```bash
cd training
```

## Building The Project For The First Time

Brightspot projects use Maven for dependency management and build configurations. To the build the project for the first time:

```bash
mvn clean install
```

## Setting Up The Environment

Included with the project is the Brightspot Training `Vagrantfile` at the root of the project. This file contains all of the configuration needed to running the Brightspot Training CMS
out of the box with pre-published data for the example Marvel Cinematic Universe site.

Before booting up the vagrant box, you should clear your box cache to ensure you are importing the most up-to-date Brightspot Training box:

```bash
rm -rf ~/.vagrant.d/boxes/brightspot-training
```

Now, boot up the vagrant:

```bash
vagrant up
```

When you boot up the vagrant box, it will attempt to mount the current directory the `Vagrantfile` is 
located in to the directory `/vagrant` within the environment. This will allow the user to symlink the compiled site `*.war` file 
to Tomcat's `webapps` folder to allow for automatic deployment each time a build completes.

To do this, you will first want to SSH into the training environment. From the same folder as your `Vagrantfile`, run this command:

```bash
vagrant ssh
```

You will now be logged in as the root user within the training vagrant. If you are not, log in as the root user:

```bash
sudo -i
```

Stop the Brightspot service beofre setting up the webapps:

```bash
sv stop brightspot
```

You will now need to actually symlink the `*.war` file into the Tomcat's webapps directory:

```bash
ln -s /vagrant/site/target/bex-training-site-1.0.0-SNAPSHOT.war /servers/brightspot/webapps/ROOT.war
```

Start the Brightspot service back up:

```bash
sv start brightspot
```

During development, you may want to keep yourself SSH'd into the training environment with logs open for debugging. You can tail the
Catalina logs and leave them open in the window:

```bash
tail -f /servers/brightspot/logs/catalina.out
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







