# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure('2') do |config|

  # Optionally set the name of the AWS account credentials to use
  aws_credentials_profile = ''

  # Customize your VirtualBox specific VM options here...
  config.vm.provider :virtualbox do |vb|

    # Set a unique box name here, or leave blank to have it assigned for you.
    vb.name = 'brightspot-training'

    # Modify advanced settings here. See documentation for details.
    # https://www.virtualbox.org/manual/ch08.html#vboxmanage-modifyvm
    vb.customize ['modifyvm', :id, '--memory', '4096']
    vb.customize ['modifyvm', :id, '--cpus', '1']
    vb.customize ['modifyvm', :id, '--ioapic', 'off']
    vb.customize ['modifyvm', :id, '--natdnshostresolver1', 'on']
    vb.customize ['modifyvm', :id, '--natdnsproxy1', 'on']
    vb.customize ['modifyvm', :id, '--chipset', 'ich9']
    vb.customize ['modifyvm', :id, '--nictype1', 'virtio']

    # Dynamically sets the box name (if not previously set) based on the
    # closest directory containing a known project root directory, OR just the
    # current directory name if no known project root directory exists in the
    # directory tree or its not a linux based file system.
    if vb.name == ''
      pwd = Dir.pwd

      if pwd.start_with?('/')
        while pwd != '/'
          if %w{.git src}.any? {|root_dir| Dir.exist?(File.join(pwd, root_dir))}
            break
          end

          pwd = File.expand_path('..', pwd)
        end
      end

      if pwd == '/'
        pwd = Dir.pwd
      end

      vb.name = File.basename(pwd).downcase.gsub(/[^0-9a-z]/, '-').gsub(/-+/, '-')
    end
  end

  # Box info. DO NOT MODIFY.
  config.vm.box = 'brightspot-training'
  config.vm.box_url = 'https://s3.amazonaws.com/brightspot-vagrant/training/brightspot-training.box'
  config.vm.hostname = 'brightspot-training'
  config.vm.network 'private_network', ip: '172.28.128.101'

  # Uses Beam settings from host machine when home directory is mounted.
  config.vm.provision "shell", inline: <<-SCRIPT
    echo "export BEAM_USER_HOME=#{File.expand_path('~')}" > /etc/profile.d/beam_user_home.sh
  SCRIPT

  # Switches to root user upon login.
  config.vm.provision "shell", inline: <<-SCRIPT
    echo 'case \"\$-\" in *i*) sudo -i ;; esac' > /home/vagrant/.bash_profile
    chown vagrant:vagrant /home/vagrant/.bash_profile
  SCRIPT

  # Sets AWS credential preferences
  config.vm.provision "shell", inline: <<-SCRIPT
    mkdir -p /etc/aws
    echo "#{aws_credentials_profile}" > /etc/aws/profile
    echo "#{File.expand_path('~')}/.aws/credentials" > /etc/aws/credential_profiles_file
  SCRIPT

end
