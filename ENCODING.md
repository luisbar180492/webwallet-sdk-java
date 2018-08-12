# Diference between the SDK node js and the webwallet SDK

The library for generating the public key technically returns
the encoding of the point X and Y. The snippet below comes from the
library elliptic (lib/elliptic/curve/base.js)


``` javascript

BasePoint.prototype._encode = function _encode(compact) {
  var len = this.curve.p.byteLength();
  var x = this.getX().toArray('be', len);

  if (compact)
    return [ this.getY().isEven() ? 0x02 : 0x03 ].concat(x);

  return [ 0x04 ].concat(x, this.getY().toArray('be', len)) ;
};

```

For instance with the secrete (private key) of all zeros, we 
have the following encoding (this comes from the tests in elliptic
test/ed25519-test.js):


  Test specs
    ed25519 derivations
A_P
{ x: '5849722E338ACED7B50C7F0E9328F9A10C847B08E40AF5C5B0577B0FD8984F15',
  y: '3B6A27BCCEB6A42D62A3A8D02A6F0D73653215771DE243A63AC048A18B59DA29' }
secret_hex
0000000000000000000000000000000000000000000000000000000000000000
A_hex
3B6A27BCCEB6A42D62A3A8D02A6F0D73653215771DE243A63AC048A18B59DA29
a_hex
5046ADC1DBA838867B2BBBFDD0C3423E58B57970B5267A90F57960924A87F156
A_P3
{ t: '03587AE5434C23EF1A643A189A9265799FFFB322B96B71A0D11D56FAABD04A69',
  x: 'EA1FE2976874FD7FB9EBC16DB22A8C4EBC44946125EA3D46EF7A8C37109A5D42',
  y: '1D1045228E54A114EAFDCFDEF3201FF015C83D7FBA26FFE642CA93121A567247',
  z: '83C4E8ACFF77ECB2C5378EADE1F8B3224F2E85D5CA5DE2DA4502E6FBF64A5114' }
a_hex
70F386460418935DECBF19D072C7E8F52EB7ED5BDA99D6A374197B57825ADE68
A_hex
33EFD1EAEDC1D22BD37E869BB7556FFCD635E8C810808BA4D14D77DD0A3FBC3C
      ✓ can compute correct a and A for secret: 0


