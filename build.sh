#!/bin/bash
mkdir build
cd build
javac -d ./ ../src/*.java
jar cfe ../tmd2obj.jar Converter *
cd ../