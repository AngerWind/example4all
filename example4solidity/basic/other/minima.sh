#!/bin/bash

yum install -y yum-utils
yum install -y vim openssh-server openssh-clients curl

cp /etc/ssh/sshd_config /etc/ssh/sshd_config.bak
sed -i 's/PermitRootLogin/#PermitRootLogin/g' /etc/ssh/sshd_config
echo "PermitRootLogin yes" >>/etc/ssh/sshd_config
systemctl restart sshd
systemctl enable sshd

yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo

yum -y install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

systemctl enable docker
systemctl start docker

docker run -d \
    -e minima_mdspassword=123 \
    -e minima_server=true \
    -v ~/minima/10001:/home/minima/data \
    -p 10001-10004:9001-9004 \
    --name minima10001 \
    --restart=no \
    -m 512m minimaglobal/minima:latest

docker run -d \
    -e minima_mdspassword=123 \
    -e minima_server=true \
    -v ~/minima/10011:/home/minima/data \
    -p 10011-10014:9001-9004 \
    --name minima10011 \
    --restart=no \
    -m 512m minimaglobal/minima:latest

docker run -d \
    -e minima_mdspassword=123 \
    -e minima_server=true \
    -v ~/minima/10021:/home/minima/data \
    -p 10021-10024:9001-9004 \
    --name minima10021 \
    --restart=no \
    -m 512m minimaglobal/minima:latest

docker run -d \
    --restart unless-stopped \
    --name watchtower \
    -e WATCHTOWER_CLEANUP=true \
    -e WATCHTOWER_TIMEOUT=60s \
    -v ~/watchtower:/var/run/docker.sock \
    --restart=always \
    containrrr/watchtower

docker container stop $(docker ps -aq) # 一键停止所有容器
