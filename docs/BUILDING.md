# Building The Training Vagrant

To contribute to the training project and package it for deployment you must complete the following clean up steps.

## Verify The CMS:

- Remove all users besides the `debug` user in Users & Roles
- Ensure the Marvel Cinematic Universe site has the following primary URL: `http://dev.training.com`
- Ensure the modified `Frost` theme is set in Front-End settings.

## Host Preliminary Checks:

_(Referenced from the Brightspot Vagrant [BUILDING.md](https://github.com/perfectsense/brightspot-vagrant/blob/master/BUILDING.md))_

**Halt the Vagrant before continuing!**

```bash
vagrant halt
```

To ensure there's no unexpected hiccups, before creating the Brightspot Vagrant box it's best to ensure 
that you have shutdown any existing vagrant instances you may have running, as well as clear out your existing 
`/etc/exports` file, making sure to back it up first. The latter can be achieved easily with the following commands:

```bash
sudo cp /etc/exports /etc/exports_bk
sudo bash -c '> /etc/exports'
```

Start up the Vagrant once again:

```bash
vagrant up
```

## Clean Up The Environment:

Once you have verified the environment is to your liking, go ahead and ssh in the environment:

```bash
vagrant ssh
sudo -i
```

Remove the webapp and unlink the .WAR file:
```bash
rm -rf /servers/brightspot/webapps/ROOT*
```

Logout of root and modify the clean up script to preserve data. (This may already be done so it is mainly a verification step):

```bash
logout
nano /home/vagrant/vagrant-clean.sh
```

Verify the following commands have been commented out or do not exist in the script:

```bash
rm -rf /opt/chef*
rm -rf /servers/solr/temp/*
rm -rf /servers/mysql-*
mysql -e "SET GLOBAL innodb_fast_shutdown = 0"
service mysql stop
rm -rf /var/lib/mysql/ib_logfile* /var/lib/mysql/master-bin.*
```

Write out the file and close. Execute the script with wipe mode enabled:

```bash
sudo -i /home/vagrant/vagrant-clean.sh --wipe
```

Reset the SSH authorized keys so that new host machines can access the virtual machine:

```bash
wget https://raw.githubusercontent.com/hashicorp/vagrant/master/keys/vagrant.pub -O ~/.ssh/authorized_keys
chmod 700 ~/.ssh
chmod 600 ~/.ssh/authorized_keys
chown -R vagrant:vagrant ~/.ssh
```

Clean the histories one last time before packaging:

```bash
sudo -i unset HISTFILE
sudo -i rm -f /root/.bash_history
sudo -i history -c
unset HISTFILE
rm -f /home/vagrant/.bash_history
history -c
```

Exit the virtual machine and package the box:

```bash
exit
vagrant package --base training --output brightspot-training.box
```

_**Note: Do Not Commit The Newly Packaged Box To The Remote Repository!!**_

## Restore Host Settings

_(Referenced from the Brightspot Vagrant [BUILDING.md](https://github.com/perfectsense/brightspot-vagrant/blob/master/BUILDING.md))_

Restore your previous /etc/exports settings.

```bash
sudo cp /etc/exports_bk /etc/exports
rm /etc/exports_bk
```


