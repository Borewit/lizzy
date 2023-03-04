#!/bin/sh
root=`dirname "$0"`
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$root/lib
java -cp "$root/lizzy.jar:$root/lib/args4j.jar:$root/classes" christophedelory.lizzy.AddToPlaylist "$*"
