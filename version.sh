#!/usr/bin/env bash
if [ -z $1 ]
then
  echo '请输入版本号'
  exit -1
fi
mvn versions:set -DnewVersion=$1
mvn versions:commit
