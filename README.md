# webwallet-sdk
Webwallet sdk java version

This is the java implementation for the nodejs sdk https://github.com/webwallet/sdk

``` java

/* Generate cryptographic keys and addresses */

KeyPairHolder keyPair = new KeyPairHolder();
System.out.println(keyPair.getPublicKey());
System.out.println(keyPair.getSecretInHexString());


Address address = new Address(keyPair.getPublicKey());
        String value = address.generate().getValue();
        System.out.println("address generated");
        System.out.println(value);

        
        
IouParamsDto iouParamsDto;
        String domain = "www";
        String source = (new KeyPairHolder()).getPublicKey();
        String target= (new KeyPairHolder()).getPublicKey();
        BigDecimal amount = new BigDecimal(100);
        BigDecimal credit = new BigDecimal(0);

        String symbol = source;
        Date active = new Date();
        Date expire = DateUtils.addDays(active,4);

        iouParamsDto = new IouParamsDto(domain, source,target, amount, credit,
                                        symbol, null,active, expire);


        IouUtil iouUtil = new IouUtil();
        IOU theIou = iouUtil.write(iouParamsDto);

        List<PrivateKey> privatekeys = new ArrayList<>();
        privatekeys.add(sourceKeys.getSecret());
        theIou.sign(privatekeys);

	String theIouJson = theIou.toString();
        System.out.println(theIouJson);        

```
