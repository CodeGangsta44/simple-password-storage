### Next features were implemented to create cryptographic secure
storage of sensitive information:

* AES-256 in GCM algorithm was chosen for encryption as one of the
  best-practise algorithm and recommended by OWASP
  
* Java Security model was used as library for algorithms 
  with Bouncy Castle security provider because it provide flexibility
  and allow implementing envelope encryption easily
  
* Sensitive data of every user 
  is encrypted by separate DEK (data encryption key)
  which is stored in database
  
* Each DEK is encrypted with KEK (key encryption key) which
  is only one for all DEKs and is stored separately
  
* KEK is stored in Vault by HashiCorp (KMS) and requested by application
  when needed
  
* Each user id is created by UUID generator (IETF RFC 4122 version 4)
  to protect users from selection by sequence ids.
  
### Possible attacks vectors

* Penetrate to OS and steal username and password
  from database from env variables
  
* Steal Vault API token from env variables
  that allows to find out the KEK and decrypt all
  DEK if database stolen
  
* Access to Vault is in development mode and use
  http connection, so data passed via it can be compromised
  
