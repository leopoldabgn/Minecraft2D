#!/usr/bin/env bash
export ARGS=`echo "$@"`
mvn package
clear
java -cp ./target/worms*.jar launcher.App "$ARGS"
