#!/bin/sh
# Maven Wrapper script for Unix/Linux/Mac
# This downloads Maven if not available and runs it

MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$(cd "$(dirname "$0")" && pwd)}"

if [ -z "$JAVA_HOME" ]; then
  JAVACMD="java"
else
  JAVACMD="$JAVA_HOME/bin/java"
fi

WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"

if [ -r "$WRAPPER_JAR" ]; then
  exec "$JAVACMD" \
    -jar "$WRAPPER_JAR" \
    "$@"
else
  echo "Maven wrapper jar not found. Downloading..."
  WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
  mkdir -p "$MAVEN_PROJECTBASEDIR/.mvn/wrapper"
  
  if command -v curl > /dev/null 2>&1; then
    curl -sL "$WRAPPER_URL" -o "$WRAPPER_JAR"
  elif command -v wget > /dev/null 2>&1; then
    wget -q "$WRAPPER_URL" -O "$WRAPPER_JAR"
  fi
  
  exec "$JAVACMD" \
    -jar "$WRAPPER_JAR" \
    "$@"
fi
