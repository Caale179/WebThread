#!/bin/bash

javac -classpath jsoup-1.12.1.jar WebGrep/sujet/WebGrep.java WebGrep/sujet/Tools.java WebGrep/sujet/WebThread.java -d bin

java -classpath  bin sujet.WebGrep jsoup-1.12.1.jar
