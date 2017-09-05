#!/bin/bash -x

# I acknowledge that this an ugly way to do this ... will learn Ant later

# Clear exec directory of .class files
rm -rf /exec/*

# Make data directory for save files
cd exec
mkdir data
cd ..

# Compile all .java files from pre-made list of all .java files into exec directoy
javac -g @argfile -d ./exec

# Run the program
cd exec
java controller/Main
