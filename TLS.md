## Following steps were executed to enable TLS:

### Configuration:

1. Created root-ca and server directories to store CA and ServerCertificate related storages, certs and keys. Detailed steps you can find in [certConfig.sh](/src/main/resources/certConfig.sh)

2. Configured application to use created certificate and newest TLS v1.3 protocol.

### Ciphersuites / protocol versions / algorithms

1. For key algorithm was chosen RSA with key size of 3072 bits
2. TLS ciphersuit:
    * Key Exchange Algorithm: **Elliptic Curve Diffie–Hellman (ECDH)**
    * Authentication Algorithm: **RSA**
    * Bulk Encryption Algorithm: **AES-256 in GCM**
    * Mac Algorithm: **SHA384**
      
**Argumentation:** TLS 1.3 protocol was used because of relevance. Also, 
v1.3 provides **ECDH** as default algorithm of key exchange. 
In addition, this combination is considered one of the 
[best](https://www.ssl.com/guide/ssl-best-practices/) 
[practices](https://www.acunetix.com/blog/articles/tls-ssl-cipher-hardening/) of TSL/SSL in 2020.

### Certificates and keys storage

Both, CA and Server certificates and keys stores in special jks-key-stores, which is locked with store-password.
Also, except certificates, key-stores storing keys, which are also encrypted with separate password. 
Both of directories have 4-0-0 permissions, which means, that only owner has reading privileges. 
Any other users does not have any privileges.