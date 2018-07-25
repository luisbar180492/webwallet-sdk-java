# Webwallet sdk design 

The webwallet is compiled using Maven and the specification of the 
dependencies are decided using the pom.xml . The resulting jar to be used is
the one with dependencies in case you are going to use the library in a non-Maven
based project.

## Primitives
There three primitives implemented:

1. Keypair
2. Address
3. IOU

### Keypair

The KeyPair name for a class is already used in the java.security package that comes
with the standard java library, so the name of chosen for the class holding and creating 
the keyPair using the Ed25519 algorithm. 

The KeyPair is generated using the open source library implementing the Ed25519 algorithm 
https://github.com/str4d/ed25519-java in the KeyPairHolder constructor.

### Address

### IOU
