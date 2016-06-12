#!/bin/sh

if [ ! $# -eq 1 ]; then
    echo "usage: sh $0 <conf_file>"
    exit 1
fi

basedir=`dirname $0`/..
basedir=`(cd $basedir;pwd)`

sh ./bin/executor.sh $basedir/$1
