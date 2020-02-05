# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure('2') do |config|
  home = File.expand_path('~')
  config.vm.synced_folder home, home, type: 'nfs'

  # Customize your VirtualBox specific VM options inside here...
  config.vm.provider :virtualbox do |vb|

    # Optionally set a unique box name here, or leave blank to have it assigned for you based on directory / git project name
    #vb.name = ''

    #Example VM option, see full list here: https://www.virtualbox.org/manual/ch08.html
    #vb.customize ['modifyvm', :id, '--memory', '4096']
  end

  # To connect to AWS services, set your default AWS region (Ex. us-east-1)
  # and the name of your AWS account credentials profile (Ex. default).
  #aws_region = 'us-east-1'
  #aws_credentials_profile = ''

  # Standard box name and location, DO NOT MODIFY.
  config.vm.box = 'brightspot'
  config.vm.box_url = 'https://s3.amazonaws.com/brightspot-vagrant/boxes/brightspot.box'

end
