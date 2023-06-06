#!/usr/bin/env bash
export ARGS=`echo "$@"`
mvn package
clear
java -cp ./target/minecraft*.jar launcher.App "$ARGS"
