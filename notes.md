# Build & Deploy Notes

`./gradlew clean build test publishToMavenLocal artifactoryPublish`

This will generate the build products in your local maven repo `~/.m2`

## Signing Requirements

To sign the products of the build, you will need to have generated a signing key using [gpg](https://www.gnupg.org/documentation/howtos.html)

The following field are required in the gradle.properties

`signing.keyId=< last 8 symbols of the key >`  
`signing.password=< password for the private key >`  
`signing.secretKeyRingFile=< file location/.gnupg/secring.gpg >`  

To see your key, `use gpg -k`

## Make a bundle

example: 

jar -cvf bundle.jar   Z80Processor-4.1.0-javadoc.jar Z80Processor-4.1.0-javadoc.jar.asc Z80Processor-4.1.0-sources.jar Z80Processor-4.1.0-sources.jar.asc Z80Processor-4.1.0.jar Z80Processor-4.1.0.jar.asc Z80Processor-4.1.0.module Z80Processor-4.1.0.module.asc Z80Processor-4.1.0.pom Z80Processor-4.1.0.pom.asc

# Manual Publish Bundle

[Publish to Sonotype / Maven Central](https://central.sonatype.org/publish/publish-manual/)

