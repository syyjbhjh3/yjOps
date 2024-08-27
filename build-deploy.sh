#!/bin/sh

REGISTRY=msa.harbor.com/library
TAG=v1.2
APP_NAME=yjops

# BUILD TOOL SETTING - INIT
if [ $1 = "init" ]; then
  export M2_HOME=/go/build-tool/apache-maven-3.9.8
  export M2=$M2_HOME/bin
  export JAVA_HOME=/go/build-tool/jdk8u382-b05-jre
  export GRADLE_HOME=/opt/gradle-8.8
  export PATH=$M2:$JAVA_HOME/bin:$GRADLE_HOME:$PATH
fi

if [ $1 = "build" ]; then
  # 1.docker build
  echo ---DOCKER build---
  gradle build -x test

  # 2.docker build
  echo ---DOCKER build---
  #docker build --platform linux/amd64 -t $REGISTRY/$APP_NAME:$TAG .

  # 3.docker images
  echo ---DOCKER images...---
  #docker images | grep $REGISTRY
fi

# DOCKER SAVE
if [ $1 = "save" ]; then
	# 1.docker save
  echo ---docker save $APP_NAME ---
  docker save -o $APP_NAME.tar $REGISTRY/$APP_NAME:$TAG
fi

if [ $1 = "send" ]; then
	TARGET_REPO=root@10.0.27.148
	TARGET_SSH_PORT=22
	TARGET_DIR=/root

  echo ---send images $APP_NAME ---
  scp -P $TARGET_SSH_PORT $APP_NAME.tar $TARGET_REPO:$TARGET_DIR/
fi
