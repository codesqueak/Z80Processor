# Build & Deploy Notes

`./gradlew clean build test publishToMavenLocal`

This will generate the build products in your local maven repo `~/.m2`

## Signing Requirements

To sign the products of the build, you will need to have generated a signing key using [gpg](https://www.gnupg.org/documentation/howtos.html)

The following field are required in the gradle.properties

`signing.keyId=< last 8 symbols of the key >`  
`signing.password=< password for the private key >`  
`signing.secretKeyRingFile=< file location/.gnupg/secring.gpg >`  

To see your key, `use gpg -k`

