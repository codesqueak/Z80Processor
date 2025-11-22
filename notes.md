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

You will; need to generate a key ring

`gpg --export-secret-keys >~/.gnupg/secring.gpg`

You will also need the short form of the key id

`gpg --list-keys --keyid-format short`


`signing.password=`   
`signing.secretKeyRingFile=/home/wherever/.gnupg/secring.gpg`   
`signing.keyId=<short form>>`
`


## Make a bundle

example: 

sign your files first

`gpg -ab Z80Processor-5.0.0.jar`  
`gpg -ab Z80Processor-5.0.0-javadoc.jar`  
`gpg -ab Z80Processor-5.0.0.module`  
`gpg -ab Z80Processor-5.0.0.pom`  
`gpg -ab Z80Processor-5.0.0-sources.jar`  

Then generate Invalid sha1 and md5 files for each

then zip

The files should be in the sub-directory com/codingrodent/microprocessor/Z80Processor/5.0.0/


# Manual Publish Bundle

[Publish to Sonotype / Maven Central](https://central.sonatype.org/publish/publish-manual/)

