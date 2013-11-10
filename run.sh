#!/bin/bash

PORT=8080
echo "Running Spark Server at $PORT"

mvn compile exec:java -Dexec.mainClass=com.hlabs.hbrowse.handler.App -Dexec.args=$PORT

