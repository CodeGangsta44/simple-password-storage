keytool -genkeypair \
  -keyalg RSA \
  -keysize 3072 \
  -alias root-ca \
  -dname "CN=Roman Dovhopoliuk,OU=IP-71,O=KPI,L=Kyiv,C=UA" \
  -ext BC:c=ca:true \
  -ext KU=keyCertSign \
  -validity 3650 \
  -keystore $CA_DIRECTORY/ca.jks \
  -storepass $CA_STORE_PASS \
  -keypass $CA_KEY_PASS

keytool -exportcert \
  -keystore $CA_DIRECTORY/ca.jks \
  -storepass $CA_STORE_PASS \
  -alias root-ca \
  -rfc \
  -file $CA_DIRECTORY/ca.pem

keytool -genkeypair \
  -keyalg RSA \
  -keysize 3072 \
  -alias localhost \
  -dname "CN=Roman Dovhopoliuk,OU=IP-71,O=KPI,L=Kyiv,C=UA" \
  -ext BC:c=ca:false \
  -ext EKU:c=serverAuth \
  -ext "SAN:c=DNS:localhost,IP:127.0.0.1" \
  -validity 3650 \
  -keystore $SERVER_DIRECTORY/server.jks \
  -storepass $SERVER_STORE_PASS \
  -keypass $SERVER_KEY_PASS

keytool -certreq \
  -keystore $SERVER_DIRECTORY/server.jks \
  -storepass $SERVER_STORE_PASS \
  -alias localhost \
  -keypass $SERVER_KEY_PASS \
  -file $SERVER_DIRECTORY/server.csr

keytool -gencert \
  -keystore $CA_DIRECTORY/ca.jks \
  -storepass $CA_STORE_PASS \
  -infile $SERVER_DIRECTORY/server.csr \
  -alias root-ca \
  -keypass $CA_KEY_PASS \
  -ext BC:c=ca:false \
  -ext EKU:c=serverAuth \
  -ext "SAN:c=DNS:localhost,IP:127.0.0.1" \
  -validity 3650 \
  -rfc \
  -outfile $SERVER_DIRECTORY/server.pem

keytool -importcert \
  -noprompt \
  -keystore $SERVER_DIRECTORY/server.jks \
  -storepass $SERVER_STORE_PASS \
  -alias root-ca \
  -keypass $CA_KEY_PASS \
  -file $CA_DIRECTORY/ca.pem

keytool -importcert \
  -noprompt \
  -keystore $SERVER_DIRECTORY/server.jks \
  -storepass $SERVER_STORE_PASS \
  -alias localhost \
  -keypass $SERVER_STORE_PASS \
  -file $SERVER_DIRECTORY/server.pem
