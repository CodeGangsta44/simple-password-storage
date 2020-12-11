### Next features were implemented to provide secure storage:

* Delegating hashing using different hashing algorithms such as
  BCrypt and Argon2 (by default) which allow easily migrate to newer algorithm if necessary

* Providing login attempts control which blocks user by ip for 24 hours
  if attempts rate was exceeded using custom listeners

* Validation of username and password which follows next rules:
    * Username must be unique and valid email address
    *  Passwords will contain at least 1 upper case letter
    *  Passwords will contain at least 1 lower case letter
    *  Passwords will contain at least 1 number or special character
    *  Passwords will contain at least 8 characters in length

* Protection from enumeration attacks by hiding information about
  existing users when username is not correct or unique while registering or login in

* Provide protection from time-based attacks using filter which wait a random time in range
  before execute search for user in database