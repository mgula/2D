#!/bin/bash -x

# I acknowledge that this an ugly way to do this ... will learn Ant later

# Compile all .java files from pre-made list of all .java files into exec directoy
javac -g @argfile -d ./exec

# Run the program
cd exec
java controller/Main
