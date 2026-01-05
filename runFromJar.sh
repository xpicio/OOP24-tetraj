#!/bin/sh

./gradlew clean  shadowJar

cp build/libs/tetraj-all.jar tetraj.jar

java -jar tetraj.jar
