![CodeQL](https://github.com/codesqueak/Z80Processor/workflows/CodeQL/badge.svg)
[![Java CI with Gradle](https://github.com/codesqueak/Z80Processor/actions/workflows/gradle.yml/badge.svg)](https://github.com/codesqueak/Z80Processor/actions/workflows/gradle.yml)
[![License: MIT](https://img.shields.io/badge/license-Apache_2.0-brightgreen.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.codingrodent.microprocessor/Z80Processor/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.codingrodent.microprocessor/Z80Processor)

# Z80 Processor in Java

Z80Processor is a an implementation of the Mostek / Zilog Z80 processor in Java

The code is not designed to be nice / clean / compact - however it is designed to be fast and easily checkable with every instruction 
on its own switch / case statement. 

If you are looking for a compact implementation, have a look at the Go implementation [here](https://github.com/codesqueak/z80)

It code has been heavily profiled using [Yourkit](https://www.yourkit.com/) while running 'real' applications to identify hotspots.

If you find this project useful, you may want to [__Buy me a Coffee!__ :coffee:](https://www.buymeacoffee.com/codesqueak) Thanks :thumbsup:

## Build

Windows

gradlew clean build test

Linux

./gradlew clean build test

The build may take a few minutes as it includes a comprehensive test suite for the Z80 instruction set.

## Java Version

Version 5.0.0 onwards of the emulator require Java 25 or above

### Include Using Maven

```
<!-- https://mvnrepository.com/artifact/com.codingrodent.microprocessor/Z80Processor -->
<dependency>
    <groupId>com.codingrodent.microprocessor</groupId>
    <artifactId>Z80Processor</artifactId>
    <version>5.0.0</version>
</dependency>
```

### Include Using Gradle

```
// https://mvnrepository.com/artifact/com.codingrodent.microprocessor/Z80Processor
compile group: 'com.codingrodent.microprocessor', name: 'Z80Processor', version: '5.0.0'
```

## Undocumented instruction

The code attempts to faithfully reproduce the numerous undocumented instructions in the Z80.  I have tested against a real device but if you find any issues, let me know.

## How to make a machine

To make a machine you need three components, the CPU, Memory and I/O.  To see a simple example, look at the test in Z80CoreTest.java.  






