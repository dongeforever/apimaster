#!/bin/sh

if [ ! $# -gt 0 ]; then
    echo "usage: sh $0 <conf_file> [debug]"
    exit 1
fi

basedir=`dirname $0`/..
basedir=`(cd $basedir;pwd)`

sh ./bin/executor.sh $basedir/$1 $2
