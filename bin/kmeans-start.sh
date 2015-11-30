#!/bin/bash

JARFILES=""
for f in ../target/dependency/*.jar; do
  JARFILES=${JARFILES}":"$f
done

java -cp $JARFILES:../target/classification.jar xof.classification.kmeans.App

