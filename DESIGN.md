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
the keyPair using the Ed25519 algorithm is KeyPairHolder. 

The KeyPair is generated using the open source library implementing the Ed25519 algorithm 
https://github.com/str4d/ed25519-java in the KeyPairHolder constructor.

Typically for handling KeyPair of private and public addresses the java standard security 
classes are used . That is why in the KeyPairHolder is a wrapper to be similar to its
equivalent in the nodejs version converting all the fields to String.

Note that we are harcoding the same scheme in the **scheme**  field. 

### Address
Unlike javascript there is no standard way to give 
optional parameter to a method. In the case of constructor
one way is to use the builder design pattern, it is used in the Address object.

Using the methods *withHash* and *withHashAlgorithms* it is possible to add the 
parameters that are optional. When the method generate is called the **address** 
is generated. Inside this call, the encoding is made. 

### IOU
The IOU class a java plain object for holding the data related to a *claim* . The claim 
is created in the class **IouParamsDto** , later IouUtil write the claim and generate an
*IOU*. This IOU can be signed passing a Map(PrivateKey and SignatureDto ) for 
signing to Iou. 

##### HashUtils

The HashingUtil class contains the key methods for encoding with the hashing algorithms.
It is called by the Address class and IOU class.

##### Adding new functionalities
 
The key classes exposed KeyPairHolder, Address and IOU. A key funcionality that is needed 
in all classes is parsing a JSON Address, IOU, or KeyPair generated in the node js library.
In order to add a new functionality this can be done adding either a public method in the existing
key classes or adding a new class. 