#!/usr/bin/env bash
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=4000,suspend=n -jar sittpuffen-api-1.0-SNAPSHOT.jar