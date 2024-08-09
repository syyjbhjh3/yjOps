#!/bin/sh

REGISTRY=msa.harbor.com/library
TAG=v0.4
APP_NAME=yjops

if [ $1 = "build" ]; then
  # 1.docker build
  echo ---DOCKER build---

  docker build --platform linux/amd64 -t $REGISTRY/$APP_NAME:$TAG .

  # 2.docker images
  echo ---DOCKER images...---

  docker images | grep $REGISTRY
fi

# DOCKER SAVE
if [ $1 = "save" ]; then
	# 1.docker save
  echo ---docker save $APP_NAME ---
  docker save -o $APP_NAME.tar $REGISTRY/$APP_NAME:$TAG
fi

if [ $1 = "send" ]; then
	TARGET_REPO=root@
	TARGET_SSH_PORT=22
	TARGET_DIR=/root

  echo ---send images $APP_NAME ---
  scp -P $TARGET_SSH_PORT $APP_NAME.tar $TARGET_REPO:$TARGET_DIR/
fi