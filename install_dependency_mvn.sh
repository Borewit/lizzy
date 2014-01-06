#!/bin/bash

# Installe les libraries Java depuis lib/ dans le dépot Maven local;
# à exécuter une seule fois,
# ensuite on peut créer le jar exécutable avec:
# mvn package -Dmaven.test.skip=true --offline

# TODO : pouvoir copier depuis le repo Maven local vers lib/

# arguments: $1 : nom de la librarie (artifactId); $2 : fichier jar; $3 : numéro version
function dependency() {
	artifactId=$1
	file=$2
	version=$3
	mvn install:install-file -Dfile=dist/lib/$file -DgroupId=${artifactId} -DartifactId=${artifactId} -Dversion=${version} \
		-Dpackaging=jar -DgeneratePom=true
}

dependency	jna	jna.jar	3.0
dependency	ffmpeg-java	ffmpeg-java.jar	20070916-0351
