#!/bin/bash
# ----------------------------------------------------------------------
# author:       yangfuhai
# email:        fuhai999@gmail.com
# use : wget https://gitee.com/JPressProjects/jpress/raw/master/install.sh && bash install.sh
# ----------------------------------------------------------------------

# abort on errors
set -e

port="$1"

# Install docker Linux
if ! [ -x "$(command -v docker)" ]; then
  echo "Detected Docker Haven't installed yet, I'm trying to install Docker ..."

  if [ -x "$(command -v yum)" ]; then
    sudo yum install -y python3-pip yum-utils device-mapper-persistent-data lvm2
    sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
    yum list docker-ce --showduplicates | sort -r
    sudo yum install docker-ce
  else
    sudo apt-get update
    sudo dpkg --configure -a
    sudo apt-get install python3-pip apt-transport-https ca-certificates curl software-properties-common
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
    sudo apt-get update
    sudo apt-get install docker-ce
  fi

  # Start the docker and start the self-startup
  sudo systemctl start docker
  sudo systemctl enable docker
fi


 # Install docker-compose
if ! [ -x "$(command -v docker-compose)" ]; then
  echo "Detected Docker-Compose Haven't installed yet, I'm trying to install Docker-Compose ..."
  if ! [ -x "$(command -v pip3)" ]; then
      curl -L https://github.com/docker/compose/releases/download/1.25.1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
      chmod +x /usr/local/bin/docker-compose
  else
      pip3 install --upgrade pip
      pip3 install docker-compose
  fi
fi


 # Start installation jpress
if [ -x "$(command -v docker)" -a -x "$(command -v docker-compose)" ]; then
  docker version
  docker-compose -version

  # Install jpress
  if [ ! -f "docker-compose.yml" ];then
    wget https://gitee.com/JPressProjects/jpress/raw/master/docker-compose.yml
  fi

  if [[ "$port" != "" ]]; then
    perl -pi -e "s/8080:8080/$port:8080/g" docker-compose.yml
  fi

  if [ ! -f "/etc/docker/daemon.json" ];then
    sudo mkdir -p /etc/docker
    echo -E '{"registry-mirrors": ["https://kn77wnbv.mirror.aliyuncs.com"]}' > /etc/docker/daemon.json
    sudo systemctl daemon-reload
    sudo systemctl restart docker
  fi

  docker-compose up -d

else

  if ! [ -x "$(command -v docker)" ]; then
    echo 'Docker If the installation fails, please detect whether your current environment (or network) is normal.'
  fi


  if ! [ -x "$(command -v docker-compose)" ]; then
    echo 'Docker-Compose If the installation fails, please detect whether your current environment (or network) is normal.'
  fi

fi

rm -rf install.sh