#!/bin/bash

./gradlew formatKotlin && ./gradlew lintKotlin && ./gradlew detekt

# Before use it, in the first time, you must guarantee some running permissions:
# chmod +x codeAnalysis.sh
#
# After that, you just need to run:
# ./codeAnalysis.sh
