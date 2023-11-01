#!/usr/bin/env bash

echo '开始编译打包'

mvn clean package -Pprod

echo '编译打包完成，开始构建镜像'
docker-compose build

echo '容器关闭'
docker-compose down

echo '启动服务q'
docker-compose up -d