#!/bin/sh
root=`dirname $0`
java -cp "$root/lizzy.jar:$root/classes" christophedelory.lizzy.ContentTypeInfo $*
