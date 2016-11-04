# Z80 Processor in Java

Z80Processor is a an implementation of the Mostek / Zilog Z80 processor in Java

## Build

(Windows)
gradlew clean build test

(Linux)
./gradlew clean build test

The build may take a few minutes as it includes a comprehensive test suite for the Z80 instruction set.


## Using Jenkins

The project includes a Jenkins file to control a pipeline build.  At present the available version of the Jacoco plugin (2.0.1 at time of writing) does not support a 'publisher'.  The build was tested using a hand built plugin from the master branch of the  [project](https://github.com/jenkinsci/jacoco-plugin)


## Undocumented instruction

The code attempts to faithfully reproduce the numerous undocumented instructions in the Z80.  I have tested against a real device but if you find any issues, let me know.

## How to make a machine

To make a machine you need three components, the CPU, Memory and I/O.  To see a simple example, look at the test in Z80CoreTest.java.  






